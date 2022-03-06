
CREATE TABLE post_user (
	id bigserial NOT NULL,
	email varchar(255) NOT NULL,
	name varchar(128) NOT NULL,
	password varchar(255) NOT NULL,
	created_at timestamp NULL,
    created_by_ip_address varchar(255) NULL,
	updated_at timestamp NULL,
	updated_by_ip_address varchar(255) NULL,
	CONSTRAINT post_user_pkey PRIMARY KEY (id),
	CONSTRAINT email_unique UNIQUE (email)
);

CREATE TABLE post (
    id bigserial NOT NULL,
    author_id int8 NOT NULL,
    content varchar(10000) NOT NULL,
    expose bool NOT NULL,
    title varchar(200) NOT NULL,
    created_at timestamp NULL,
    created_by_ip_address varchar(255) NULL,
    updated_at timestamp NULL,
    updated_by_ip_address varchar(255) NULL,
    CONSTRAINT post_pkey PRIMARY KEY (id),
    CONSTRAINT author_fkey FOREIGN KEY (author_id) REFERENCES post_user(id)
);


CREATE TABLE favorite (
	id bigserial NOT NULL,
	post_id int8 NOT NULL,
	user_id int8 NOT NULL,
    created_at timestamp NULL,
	CONSTRAINT favorite_pkey PRIMARY KEY (id),
	CONSTRAINT post_user_fkey FOREIGN KEY (user_id) REFERENCES post_user(id) ON DELETE CASCADE,
	CONSTRAINT post_fkey FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE
);

CREATE TABLE album (
	id bigserial NOT NULL,
	author_id int8 NULL,
	description varchar(500) NULL,
	title varchar(200) NULL,
	CONSTRAINT album_pkey PRIMARY KEY (id),
	CONSTRAINT author_fkey FOREIGN KEY (author_id) REFERENCES post_user(id)
);

CREATE TABLE album_posts (
	album_id int8 NOT NULL,
	posts_id int8 NOT NULL,
	CONSTRAINT posts_id_unique UNIQUE (posts_id),
	CONSTRAINT posts_id_fkey FOREIGN KEY (posts_id) REFERENCES post(id),
	CONSTRAINT album_id_fkey FOREIGN KEY (album_id) REFERENCES album(id)
);

