-- PUBLIC SCHEMA
CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE public.product (
  id BIGINT NOT NULL,
  name VARCHAR(63) NOT NULL,
  CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE public.region (
  id BIGINT NOT NULL,
  name VARCHAR(63) NOT NULL,
  lat DOUBLE PRECISION NOT NULL,
  lon DOUBLE PRECISION NOT NULL,
  CONSTRAINT pk_region PRIMARY KEY (id)
);

ALTER TABLE public.product ADD CONSTRAINT uq_product_name UNIQUE (name);

ALTER TABLE public.region ADD CONSTRAINT uq_region_name UNIQUE (name);


-- PARSER SCHEMA
CREATE SCHEMA IF NOT EXISTS vk_parser;

CREATE TABLE vk_parser.group (
  id BIGINT NOT NULL,
  last_post BIGINT NOT NULL,
  CONSTRAINT pk_group PRIMARY KEY (id)
);

CREATE TABLE vk_parser.group_regions (
    group_id BIGINT NOT NULL,
    region_id BIGINT NOT NULL,
    CONSTRAINT pk_group_regions PRIMARY KEY (group_id, region_id)
);

ALTER TABLE vk_parser.group_regions ADD CONSTRAINT fk_vk_group_regions_group FOREIGN KEY (group_id) REFERENCES vk_parser.group (id);
ALTER TABLE vk_parser.group_regions ADD CONSTRAINT fk_vk_group_regions_region FOREIGN KEY (region_id) REFERENCES public.region (id);


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
  CONSTRAINT pk_food_post PRIMARY KEY (id)
);

CREATE TABLE ibot.food_post_regions (
    food_post_id BIGINT NOT NULL,
    region_id BIGINT NOT NULL,
    CONSTRAINT pk_food_post_regions PRIMARY KEY (food_post_id, region_id)
);

CREATE TABLE ibot.food_post_products (
    food_post_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT pk_food_post_products PRIMARY KEY (food_post_id, product_id)
);

ALTER TABLE ibot.food_post_regions ADD CONSTRAINT fk_ibot_food_post_regions_user FOREIGN KEY (food_post_id) REFERENCES ibot.food_post (id);
ALTER TABLE ibot.food_post_regions ADD CONSTRAINT fk_ibot_food_post_regions_region FOREIGN KEY (region_id) REFERENCES public.region (id);

ALTER TABLE ibot.food_post_products ADD CONSTRAINT fk_ibot_food_post_products_user FOREIGN KEY (food_post_id) REFERENCES ibot.food_post (id);
ALTER TABLE ibot.food_post_products ADD CONSTRAINT fk_ibot_food_post_products_product FOREIGN KEY (product_id) REFERENCES public.product (id);


-- VK_BOT SCHEMA
CREATE SCHEMA IF NOT EXISTS vk_bot;

CREATE TABLE vk_bot.user (
  id BIGINT NOT NULL,
  session_active BOOLEAN NOT NULL,
  session_step INTEGER NOT NULL,
  session_name VARCHAR(63) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE vk_bot.place (
  id BIGINT NOT NULL,
  name VARCHAR(63) NOT NULL,
  radius INTEGER NOT NULL,
  user_id BIGINT NOT NULL,
  lat DOUBLE PRECISION NOT NULL,
  lon DOUBLE PRECISION NOT NULL,
  CONSTRAINT pk_place PRIMARY KEY (id)
);

CREATE TABLE vk_bot.user_products (
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT pk_user_products PRIMARY KEY (user_id, product_id)
);

ALTER TABLE vk_bot.place ADD CONSTRAINT uq_vk_place_name_user_id UNIQUE (name, user_id);

ALTER TABLE vk_bot.place ADD CONSTRAINT fk_vk_place_user FOREIGN KEY (user_id) REFERENCES vk_bot.user (id);

ALTER TABLE vk_bot.user_products ADD CONSTRAINT fk_vk_user_products_user FOREIGN KEY (user_id) REFERENCES vk_bot.user (id);
ALTER TABLE vk_bot.user_products ADD CONSTRAINT fk_vk_user_products_product FOREIGN KEY (product_id) REFERENCES public.product (id);


-- TG_BOT SCHEMA
CREATE SCHEMA IF NOT EXISTS tg_bot;

CREATE TABLE tg_bot.user (
  id BIGINT NOT NULL,
  session_active BOOLEAN NOT NULL,
  session_step INTEGER NOT NULL,
  session_name VARCHAR(63) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE tg_bot.place (
  id BIGINT NOT NULL,
  name VARCHAR(63) NOT NULL,
  radius INTEGER NOT NULL,
  user_id BIGINT NOT NULL,
  lat DOUBLE PRECISION NOT NULL,
  lon DOUBLE PRECISION NOT NULL,
  CONSTRAINT pk_place PRIMARY KEY (id)
);

CREATE TABLE tg_bot.user_products (
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT pk_user_products PRIMARY KEY (user_id, product_id)
);

ALTER TABLE tg_bot.place ADD CONSTRAINT uq_tg_place_name_user_id UNIQUE (name, user_id);

ALTER TABLE tg_bot.place ADD CONSTRAINT fk_tg_place_user FOREIGN KEY (user_id) REFERENCES tg_bot.user (id);

ALTER TABLE tg_bot.user_products ADD CONSTRAINT fk_tg_user_products_user FOREIGN KEY (user_id) REFERENCES tg_bot.user (id);
ALTER TABLE tg_bot.user_products ADD CONSTRAINT fk_tg_user_products_product FOREIGN KEY (product_id) REFERENCES public.product (id);
