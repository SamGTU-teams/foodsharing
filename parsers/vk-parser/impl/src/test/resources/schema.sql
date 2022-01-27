CREATE TABLE public.region (
    id BIGINT NOT NULL,
    name VARCHAR(63) NOT NULL,
    lat DOUBLE PRECISION,
    lon DOUBLE PRECISION,
    CONSTRAINT pk_region PRIMARY KEY (id)
);

CREATE TABLE public.category (
    id BIGINT NOT NULL,
    name VARCHAR(63) NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE public.product (
    id BIGINT NOT NULL,
    name VARCHAR(63) NOT NULL,
    category_id BIGINT,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

ALTER TABLE public.category ADD CONSTRAINT uq_category_name UNIQUE (name);

ALTER TABLE public.product ADD CONSTRAINT uq_product_name UNIQUE (name);

ALTER TABLE public.product ADD CONSTRAINT FK_PRODUCT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES public.category (id);

ALTER TABLE public.region ADD CONSTRAINT uq_region_name UNIQUE (name);

CREATE SCHEMA IF NOT EXISTS vk_parser;

CREATE TABLE vk_parser."group" (
    id INTEGER NOT NULL,
    server_id INTEGER,
    access_token VARCHAR(255),
    secret_key VARCHAR(255),
    confirmation_code VARCHAR(255) NOT NULL,
    CONSTRAINT pk_vk_group PRIMARY KEY (id)
);

CREATE TABLE vk_parser.group_regions (
    group_id INTEGER NOT NULL,
    region_id BIGINT NOT NULL,
    CONSTRAINT pk_group_regions PRIMARY KEY (group_id, region_id)
);

ALTER TABLE vk_parser.group_regions ADD CONSTRAINT fk_vk_group_regions_group FOREIGN KEY (group_id) REFERENCES vk_parser."group" (id);
ALTER TABLE vk_parser.group_regions ADD CONSTRAINT fk_vk_group_regions_region FOREIGN KEY (region_id) REFERENCES public.region (id);
