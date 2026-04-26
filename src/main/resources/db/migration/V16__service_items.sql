CREATE TABLE IF NOT EXISTS service_items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    short_description VARCHAR(1000),
    full_description TEXT,
    category VARCHAR(100),
    icon_key VARCHAR(50),
    image_url VARCHAR(500),
    language VARCHAR(10) DEFAULT 'EN',
    status VARCHAR(50) DEFAULT 'PUBLISHED',
    display_order INTEGER DEFAULT 0,
    featured BOOLEAN DEFAULT false,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

INSERT INTO service_items (name, slug, short_description, full_description, category, icon_key, image_url, display_order, featured)
VALUES
('Website Development', 'website-development', 'Modern websites built for speed, trust, and conversion.', 'We build responsive business websites, company platforms, landing pages, and content-driven websites with clean UI.', 'Web Development', 'code', 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=1200&q=80', 1, true),
('Business Automation', 'business-automation', 'Automate repetitive operations and reduce manual work.', 'We design systems that help businesses manage bookings, customer records, payments, workflows, and reports.', 'Automation', 'workflow', 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=1200&q=80', 2, true),
('Brand & Product Strategy', 'brand-and-product-strategy', 'Clarify your offer, market position, and launch direction.', 'We help you structure products, services, messaging, funnels, and customer journeys.', 'Strategy', 'target', 'https://images.unsplash.com/photo-1552664730-d307ca884978?auto=format&fit=crop&w=1200&q=80', 3, true),
('E-Commerce Platforms', 'ecommerce-platforms', 'Sell products and services with a polished online store.', 'We create online stores, catalogs, payment-ready platforms, and product pages.', 'E-commerce', 'shopping-cart', 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?auto=format&fit=crop&w=1200&q=80', 4, true),
('SEO & Content Systems', 'seo-and-content-systems', 'Improve visibility with structured content and search-ready pages.', 'We create SEO-friendly page architecture, blog structures, and landing pages.', 'Marketing', 'search', 'https://images.unsplash.com/photo-1432888622747-4eb9a8efeb07?auto=format&fit=crop&w=1200&q=80', 5, true),
('Custom Software Solutions', 'custom-software-solutions', 'Tailored software for schools, agencies, transport, booking, and operations.', 'We build custom web applications using modern frontend, backend, and database systems.', 'Software', 'layers', 'https://images.unsplash.com/photo-1515879218367-8466d910aaa4?auto=format&fit=crop&w=1200&q=80', 6, true),
('Mobile App Development', 'mobile-app-development', 'Native and cross-platform mobile apps for iOS and Android.', 'We design and develop mobile applications with seamless user experiences.', 'Mobile', 'smartphone', 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?auto=format&fit=crop&w=1200&q=80', 7, true);