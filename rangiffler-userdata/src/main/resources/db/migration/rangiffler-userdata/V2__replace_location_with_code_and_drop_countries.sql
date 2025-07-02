-- 1. Удаляем внешний ключ, если он есть
ALTER TABLE "user"
DROP COLUMN IF  EXISTS location_id;

-- 2. Добавляем новое поле "code"
ALTER TABLE "user"
    DROP COLUMN IF EXISTS code;

-- 3. Удаляем таблицу "countries"
DROP TABLE IF EXISTS countries;

-- (опционально) Передаём владельца, если нужно
ALTER TABLE "user"
    OWNER TO postgres;