package ru.mkryuchkov.blogya;

import org.hamcrest.Condition;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;

import static org.hamcrest.Condition.matched;
import static org.hamcrest.Condition.notMatched;
import static org.hamcrest.beans.PropertyUtil.NO_ARGUMENTS;

/**
 * A substitute for Hamcrest HasPropertyWithValue matcher, which still does not work with records as of version 3.0.
 * Taken from <a href="https://stackoverflow.com/questions/66982522/how-can-i-assert-hasproperty-with-a-java-record">StackOverflow</a>
 */
public class HasRecordComponentWithValue<T> extends TypeSafeDiagnosingMatcher<T> {
    private static final Condition.Step<RecordComponent, Method> WITH_READ_METHOD = withReadMethod();
    private final String componentName;
    private final Matcher<Object> valueMatcher;

    public HasRecordComponentWithValue(String componentName, Matcher<?> valueMatcher) {
        this.componentName = componentName;
        this.valueMatcher = nastyGenericsWorkaround(valueMatcher);
    }

    @Override
    public boolean matchesSafely(T bean, Description mismatch) {
        return recordComponentOn(bean, mismatch)
                .and(WITH_READ_METHOD)
                .and(withPropertyValue(bean))
                .matching(valueMatcher, "record component'" + componentName + "' ");
    }

    private Condition.Step<Method, Object> withPropertyValue(final T bean) {
        return (readMethod, mismatch) -> {
            try {
                return matched(readMethod.invoke(bean, NO_ARGUMENTS), mismatch);
            } catch (Exception e) {
                mismatch.appendText(e.getMessage());
                return notMatched();
            }
        };
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("hasRecordComponent(").appendValue(componentName).appendText(", ")
                .appendDescriptionOf(valueMatcher).appendText(")");
    }

    private Condition<RecordComponent> recordComponentOn(T bean, Description mismatch) {
        RecordComponent[] recordComponents = bean.getClass().getRecordComponents();
        for(RecordComponent comp : recordComponents) {
            if(comp.getName().equals(componentName)) {
                return matched(comp, mismatch);
            }
        }
        mismatch.appendText("No record component \"" + componentName + "\"");
        return notMatched();
    }


    @SuppressWarnings("unchecked")
    private static Matcher<Object> nastyGenericsWorkaround(Matcher<?> valueMatcher) {
        return (Matcher<Object>) valueMatcher;
    }

    private static Condition.Step<RecordComponent,Method> withReadMethod() {
        return (property, mismatch) -> {
            final Method readMethod = property.getAccessor();
            if (null == readMethod) {
                mismatch.appendText("record component \"" + property.getName() + "\" is not readable");
                return notMatched();
            }
            return matched(readMethod, mismatch);
        };
    }

    public static <T> Matcher<T> hasRecordComponent(String componentName, Matcher<?> valueMatcher) {
        return new HasRecordComponentWithValue<>(componentName, valueMatcher);
    }
}
