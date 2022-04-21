-- PUBLIC SCHEMA
CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE public.product (
    id BIGINT NOT NULL,
    name VARCHAR(63) NOT NULL,
    category_id BIGINT,
    CONSTRAINT PK_PRODUCT PRIMARY KEY (id)
);

CREATE TABLE public.category (
    id BIGINT NOT NULL,
    name VARCHAR(63) NOT NULL,
    CONSTRAINT PK_CATEGORY PRIMARY KEY (id)
);

CREATE TABLE public.region (
    id BIGINT NOT NULL,
    name VARCHAR(63) NOT NULL,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    CONSTRAINT PK_REGION PRIMARY KEY (id)
);

ALTER TABLE public.product ADD CONSTRAINT UQ_PRODUCT_NAME UNIQUE (name);

ALTER TABLE public.category ADD CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name);

ALTER TABLE public.product ADD CONSTRAINT FK_PRODUCT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES public.category (id);

ALTER TABLE public.region ADD CONSTRAINT UQ_REGION_NAME UNIQUE (name);


-- PARSER SCHEMA
CREATE SCHEMA IF NOT EXISTS vk_parser;

CREATE TABLE vk_parser.group (
    id INTEGER NOT NULL,
    server_id INTEGER,
    access_token VARCHAR(255),
    secret_key VARCHAR(255),
    confirmation_code VARCHAR(255) NOT NULL,
    CONSTRAINT PK_GROUP PRIMARY KEY (id)
);

CREATE TABLE vk_parser.group_regions (
    group_id INTEGER NOT NULL,
    region_id BIGINT NOT NULL,
    CONSTRAINT PK_GROUP_REGIONS PRIMARY KEY (group_id, region_id)
);

ALTER TABLE vk_parser.group_regions ADD CONSTRAINT FK_VK_GROUP_REGIONS_GROUP FOREIGN KEY (group_id) REFERENCES vk_parser.group (id);
ALTER TABLE vk_parser.group_regions ADD CONSTRAINT FK_VK_GROUP_REGIONS_REGION FOREIGN KEY (region_id) REFERENCES public.region (id);


-- IBOT SCHEMA
CREATE SCHEMA IF NOT EXISTS ibot;

CREATE TABLE ibot.food_post (
    id BIGINT NOT NULL,
    url VARCHAR(255) NOT NULL,
    date TIMESTAMP WITHOUT TIME ZONE,
    text VARCHAR(1023),
    attachments VARCHAR(1023),
    lat DOUBLE PRECISION,
    lon DOUBLE PRECISION,
    CONSTRAINT PK_FOOD_POST PRIMARY KEY (id)
);

CREATE TABLE ibot.food_post_regions (
    food_post_id BIGINT NOT NULL,
    region_id BIGINT NOT NULL,
    CONSTRAINT PK_FOOD_POST_REGIONS PRIMARY KEY (food_post_id, region_id)
);

CREATE TABLE ibot.food_post_products (
    food_post_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT PK_FOOD_POST_PRODUCTS PRIMARY KEY (food_post_id, product_id)
);

ALTER TABLE ibot.food_post_regions ADD CONSTRAINT FK_IBOT_FOOD_POST_REGIONS_USER FOREIGN KEY (food_post_id) REFERENCES ibot.food_post (id);
ALTER TABLE ibot.food_post_regions ADD CONSTRAINT FK_IBOT_FOOD_POST_REGIONS_REGION FOREIGN KEY (region_id) REFERENCES public.region (id);

ALTER TABLE ibot.food_post_products ADD CONSTRAINT FK_IBOT_FOOD_POST_PRODUCTS_USER FOREIGN KEY (food_post_id) REFERENCES ibot.food_post (id);
ALTER TABLE ibot.food_post_products ADD CONSTRAINT FK_IBOT_FOOD_POST_PRODUCTS_PRODUCT FOREIGN KEY (product_id) REFERENCES public.product (id);


-- VK_BOT SCHEMA
CREATE SCHEMA IF NOT EXISTS vk_bot;

CREATE TABLE vk_bot.user (
    id BIGINT NOT NULL,
    session_active BOOLEAN NOT NULL,
    session_step INTEGER NOT NULL,
    session_name VARCHAR(63) NOT NULL,
    CONSTRAINT PK_USER PRIMARY KEY (id)
);

CREATE TABLE vk_bot.place (
    id BIGINT NOT NULL,
    name VARCHAR(63) NOT NULL,
    radius INTEGER NOT NULL,
    user_id BIGINT NOT NULL,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    CONSTRAINT PK_PLACE PRIMARY KEY (id)
);

CREATE TABLE vk_bot.user_products (
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT PK_USER_PRODUCTS PRIMARY KEY (user_id, product_id)
);

ALTER TABLE vk_bot.place ADD CONSTRAINT UQ_VK_PLACE_NAME_USER_ID UNIQUE (name, user_id);

ALTER TABLE vk_bot.place ADD CONSTRAINT FK_VK_PLACE_USER FOREIGN KEY (user_id) REFERENCES vk_bot.user (id);

ALTER TABLE vk_bot.user_products ADD CONSTRAINT FK_VK_USER_PRODUCTS_USER FOREIGN KEY (user_id) REFERENCES vk_bot.user (id);
ALTER TABLE vk_bot.user_products ADD CONSTRAINT FK_VK_USER_PRODUCTS_PRODUCT FOREIGN KEY (product_id) REFERENCES public.product (id);


-- TG_BOT SCHEMA
CREATE SCHEMA IF NOT EXISTS tg_bot;

CREATE TABLE tg_bot.user (
    id BIGINT NOT NULL,
    session_active BOOLEAN NOT NULL,
    session_step INTEGER NOT NULL,
    session_name VARCHAR(63) NOT NULL,
    CONSTRAINT PK_USER PRIMARY KEY (id)
);

CREATE TABLE tg_bot.place (
    id BIGINT NOT NULL,
    name VARCHAR(63) NOT NULL,
    radius INTEGER NOT NULL,
    user_id BIGINT NOT NULL,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    CONSTRAINT PK_PLACE PRIMARY KEY (id)
);

CREATE TABLE tg_bot.user_products (
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT PK_USER_PRODUCTS PRIMARY KEY (user_id, product_id)
);

ALTER TABLE tg_bot.place ADD CONSTRAINT UQ_TG_PLACE_NAME_USER_ID UNIQUE (name, user_id);

ALTER TABLE tg_bot.place ADD CONSTRAINT FK_TG_PLACE_USER FOREIGN KEY (user_id) REFERENCES tg_bot.user (id);

ALTER TABLE tg_bot.user_products ADD CONSTRAINT FK_TG_USER_PRODUCTS_USER FOREIGN KEY (user_id) REFERENCES tg_bot.user (id);
ALTER TABLE tg_bot.user_products ADD CONSTRAINT FK_TG_USER_PRODUCTS_PRODUCT FOREIGN KEY (product_id) REFERENCES public.product (id);
