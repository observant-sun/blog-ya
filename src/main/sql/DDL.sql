-- DROP TABLE IF EXISTS public.post;

CREATE TABLE public.post (
                             id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                             title varchar NULL,
                             body text NULL,
                             likes int4 DEFAULT 0 NOT NULL,
                             created timestamp DEFAULT now() NOT NULL,
                             updated timestamp DEFAULT now() NOT NULL,
                             image_uuid varchar NULL,
                             CONSTRAINT post_pk PRIMARY KEY (id)
);

-- DROP TABLE IF EXISTS public.post_comment;

CREATE TABLE public.post_comment (
                                     id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                     post_id int8 NOT NULL,
                                     "text" text NULL,
                                     created timestamp DEFAULT now() NOT NULL,
                                     updated timestamp DEFAULT now() NOT NULL,
                                     CONSTRAINT post_comment_pk PRIMARY KEY (id)
);
CREATE INDEX post_comment_post_id_index ON public.post_comment USING btree (post_id);

-- DROP TABLE IF EXISTS public.post_tag;

CREATE TABLE public.post_tag (
                                 post_id int8 NOT NULL,
                                 tag varchar NOT NULL,
                                 CONSTRAINT post_tag_pk PRIMARY KEY (post_id, tag)
);
CREATE INDEX post_tag_post_id_index ON public.post_tag USING btree (post_id);