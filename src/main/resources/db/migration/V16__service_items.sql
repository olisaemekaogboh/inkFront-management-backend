-- ==========================================
-- Migration V16: Update Service Items
-- ==========================================
-- This migration adds new columns to service_items
-- and inserts/updates service data
-- Uses IF NOT EXISTS to avoid errors
-- ==========================================

-- ==========================================
-- PART 1: Add missing columns (safe to run)
-- ==========================================

DO $$
BEGIN
    -- Add image_url if it doesn't exist
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'service_items'
        AND column_name = 'image_url'
    ) THEN
        ALTER TABLE service_items ADD COLUMN image_url VARCHAR(500);
        RAISE NOTICE 'Added column: image_url';
    END IF;

    -- Add active if it doesn't exist
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'service_items'
        AND column_name = 'active'
    ) THEN
        ALTER TABLE service_items ADD COLUMN active BOOLEAN DEFAULT true;
        RAISE NOTICE 'Added column: active';
    END IF;

    -- Extend slug length if needed
    -- Check if column exists and its max length
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'service_items'
        AND column_name = 'slug'
        AND character_maximum_length < 255
    ) THEN
        ALTER TABLE service_items ALTER COLUMN slug TYPE VARCHAR(255);
        RAISE NOTICE 'Extended slug column length to 255';
    END IF;

    -- Extend name length if needed
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'service_items'
        AND column_name = 'name'
        AND character_maximum_length < 255
    ) THEN
        ALTER TABLE service_items ALTER COLUMN name TYPE VARCHAR(255);
        RAISE NOTICE 'Extended name column length to 255';
    END IF;

    -- Extend short_description if needed
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'service_items'
        AND column_name = 'short_description'
        AND character_maximum_length < 1000
    ) THEN
        ALTER TABLE service_items ALTER COLUMN short_description TYPE VARCHAR(1000);
        RAISE NOTICE 'Extended short_description column length to 1000';
    END IF;

    -- Modify default for language if needed
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'service_items'
        AND column_name = 'language'
        AND column_default IS NULL
    ) THEN
        ALTER TABLE service_items ALTER COLUMN language SET DEFAULT 'EN';
        RAISE NOTICE 'Set default for language column';
    END IF;

    -- Modify default for status if needed
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'service_items'
        AND column_name = 'status'
        AND column_default IS NULL
    ) THEN
        ALTER TABLE service_items ALTER COLUMN status SET DEFAULT 'PUBLISHED';
        RAISE NOTICE 'Set default for status column';
    END IF;

    -- Add any missing indexes (safe to run)
    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_service_items_slug') THEN
        CREATE INDEX idx_service_items_slug ON service_items(slug);
        RAISE NOTICE 'Created index: idx_service_items_slug';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_service_items_language') THEN
        CREATE INDEX idx_service_items_language ON service_items(language);
        RAISE NOTICE 'Created index: idx_service_items_language';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_service_items_status') THEN
        CREATE INDEX idx_service_items_status ON service_items(status);
        RAISE NOTICE 'Created index: idx_service_items_status';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_service_items_display_order') THEN
        CREATE INDEX idx_service_items_display_order ON service_items(display_order);
        RAISE NOTICE 'Created index: idx_service_items_display_order';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_service_items_featured') THEN
        CREATE INDEX idx_service_items_featured ON service_items(featured);
        RAISE NOTICE 'Created index: idx_service_items_featured';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_service_items_category') THEN
        CREATE INDEX idx_service_items_category ON service_items(category);
        RAISE NOTICE 'Created index: idx_service_items_category';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_service_items_active') THEN
        CREATE INDEX idx_service_items_active ON service_items(active) WHERE active = true;
        RAISE NOTICE 'Created index: idx_service_items_active';
    END IF;
END $$;

-- ==========================================
-- PART 2: Insert/Update Service Data
-- ==========================================

-- Use UPSERT (INSERT ON CONFLICT) to handle duplicates
INSERT INTO service_items (
    name,
    slug,
    short_description,
    full_description,
    category,
    icon_key,
    image_url,
    language,
    status,
    display_order,
    featured,
    active,
    created_at,
    updated_at
) VALUES
(
    'Website Development',
    'website-development',
    'Modern websites built for speed, trust, and conversion.',
    'We build responsive business websites, company platforms, landing pages, and content-driven websites with clean UI.',
    'Web Development',
    'code',
    'https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=1200&q=80',
    'EN',
    'PUBLISHED',
    1,
    true,
    true,
    NOW(),
    NOW()
),
(
    'Business Automation',
    'business-automation',
    'Automate repetitive operations and reduce manual work.',
    'We design systems that help businesses manage bookings, customer records, payments, workflows, and reports.',
    'Automation',
    'workflow',
    'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=1200&q=80',
    'EN',
    'PUBLISHED',
    2,
    true,
    true,
    NOW(),
    NOW()
),
(
    'Brand & Product Strategy',
    'brand-and-product-strategy',
    'Clarify your offer, market position, and launch direction.',
    'We help you structure products, services, messaging, funnels, and customer journeys.',
    'Strategy',
    'target',
    'https://images.unsplash.com/photo-1552664730-d307ca884978?auto=format&fit=crop&w=1200&q=80',
    'EN',
    'PUBLISHED',
    3,
    true,
    true,
    NOW(),
    NOW()
),
(
    'E-Commerce Platforms',
    'ecommerce-platforms',
    'Sell products and services with a polished online store.',
    'We create online stores, catalogs, payment-ready platforms, and product pages.',
    'E-commerce',
    'shopping-cart',
    'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?auto=format&fit=crop&w=1200&q=80',
    'EN',
    'PUBLISHED',
    4,
    true,
    true,
    NOW(),
    NOW()
),
(
    'SEO & Content Systems',
    'seo-and-content-systems',
    'Improve visibility with structured content and search-ready pages.',
    'We create SEO-friendly page architecture, blog structures, and landing pages.',
    'Marketing',
    'search',
    'https://images.unsplash.com/photo-1432888622747-4eb9a8efeb07?auto=format&fit=crop&w=1200&q=80',
    'EN',
    'PUBLISHED',
    5,
    true,
    true,
    NOW(),
    NOW()
),
(
    'Custom Software Solutions',
    'custom-software-solutions',
    'Tailored software for schools, agencies, transport, booking, and operations.',
    'We build custom web applications using modern frontend, backend, and database systems.',
    'Software',
    'layers',
    'https://images.unsplash.com/photo-1515879218367-8466d910aaa4?auto=format&fit=crop&w=1200&q=80',
    'EN',
    'PUBLISHED',
    6,
    true,
    true,
    NOW(),
    NOW()
),
(
    'Mobile App Development',
    'mobile-app-development',
    'Native and cross-platform mobile apps for iOS and Android.',
    'We design and develop mobile applications with seamless user experiences.',
    'Mobile',
    'smartphone',
    'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?auto=format&fit=crop&w=1200&q=80',
    'EN',
    'PUBLISHED',
    7,
    true,
    true,
    NOW(),
    NOW()
)
ON CONFLICT (slug)
DO UPDATE SET
    name = EXCLUDED.name,
    short_description = EXCLUDED.short_description,
    full_description = EXCLUDED.full_description,
    category = EXCLUDED.category,
    icon_key = EXCLUDED.icon_key,
    image_url = EXCLUDED.image_url,
    language = EXCLUDED.language,
    status = EXCLUDED.status,
    display_order = EXCLUDED.display_order,
    featured = EXCLUDED.featured,
    active = EXCLUDED.active,
    updated_at = NOW();

-- ==========================================
-- PART 3: Verification
-- ==========================================

DO $$
DECLARE
    record_count integer;
BEGIN
    SELECT COUNT(*) INTO record_count FROM service_items;
    RAISE NOTICE 'Migration V16 completed. Total service items: %', record_count;
END $$;

-- Add table comment if not exists
COMMENT ON TABLE service_items IS 'Services offered by the agency';