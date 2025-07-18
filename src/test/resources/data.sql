-- DROP TABLE IF EXISTS public.post;

CREATE TABLE public.post (
                             id int8 GENERATED ALWAYS AS IDENTITY NOT NULL,
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
                                     id int8 GENERATED ALWAYS AS IDENTITY NOT NULL,
                                     post_id int8 NOT NULL,
                                     "text" text NULL,
                                     created timestamp DEFAULT now() NOT NULL,
                                     updated timestamp DEFAULT now() NOT NULL,
                                     CONSTRAINT post_comment_pk PRIMARY KEY (id)
);

-- DROP TABLE IF EXISTS public.post_tag;

CREATE TABLE public.post_tag (
                                 post_id int8 NOT NULL,
                                 tag varchar NOT NULL
);