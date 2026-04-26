INSERT INTO hero_sections (
    title, subtitle, body, background_image_url, placement,
    primary_button_label, primary_button_url,
    secondary_button_label, secondary_button_url,
    language, status, featured, display_order,
    created_at, updated_at
)
SELECT
    'Build. Launch. Grow with InFront',
    'Digital products, websites, automation, and brand systems for ambitious businesses.',
    'We help businesses design strong online platforms, launch products faster, attract better customers, and manage growth with modern technology.',
    'https://images.unsplash.com/photo-1497366754035-f200968a6e72',
    'HOME',
    'Start a Project',
    '/contact',
    'View Services',
    '/services',
    'EN',
    'PUBLISHED',
    TRUE,
    1,
    NOW(),
    NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM hero_sections WHERE placement = 'HOME' AND language = 'EN'
);

INSERT INTO service_items (
    slug, name, short_description, full_description, category, icon_key,
    language, status, display_order, featured,
    created_at, updated_at
)
SELECT * FROM (
    VALUES
    (
        'website-development',
        'Website Development',
        'Modern websites built for speed, trust, and conversion.',
        'We build responsive business websites, company platforms, landing pages, and content-driven websites with clean UI, strong performance, SEO structure, and admin-friendly content management.',
        'Web Development',
        'code',
        'EN',
        'PUBLISHED',
        1,
        TRUE,
        NOW(),
        NOW()
    ),
    (
        'business-automation',
        'Business Automation',
        'Automate repetitive operations and reduce manual work.',
        'We design systems that help businesses manage bookings, customer records, payments, workflows, notifications, reports, and internal processes more efficiently.',
        'Automation',
        'workflow',
        'EN',
        'PUBLISHED',
        2,
        TRUE,
        NOW(),
        NOW()
    ),
    (
        'brand-and-product-strategy',
        'Brand & Product Strategy',
        'Clarify your offer, market position, and launch direction.',
        'We help you structure products, services, messaging, funnels, and customer journeys so your business communicates clearly and sells better.',
        'Strategy',
        'target',
        'EN',
        'PUBLISHED',
        3,
        TRUE,
        NOW(),
        NOW()
    ),
    (
        'ecommerce-platforms',
        'E-commerce Platforms',
        'Sell products and services with a polished online store.',
        'We create online stores, catalogs, payment-ready platforms, and product pages that help businesses receive orders and build customer trust.',
        'E-commerce',
        'shopping-cart',
        'EN',
        'PUBLISHED',
        4,
        TRUE,
        NOW(),
        NOW()
    ),
    (
        'seo-and-content-systems',
        'SEO & Content Systems',
        'Improve visibility with structured content and search-ready pages.',
        'We create SEO-friendly page architecture, reusable content sections, blog structures, metadata, and landing pages that help your business become easier to find.',
        'Marketing',
        'search',
        'EN',
        'PUBLISHED',
        5,
        TRUE,
        NOW(),
        NOW()
    ),
    (
        'custom-software-solutions',
        'Custom Software Solutions',
        'Tailored software for schools, agencies, transport, booking, and operations.',
        'We build custom web applications using modern frontend, backend, database, authentication, and admin dashboard systems for real business workflows.',
        'Software',
        'layers',
        'EN',
        'PUBLISHED',
        6,
        TRUE,
        NOW(),
        NOW()
    )
) AS seed (
    slug, name, short_description, full_description, category, icon_key,
    language, status, display_order, featured, created_at, updated_at
)
WHERE NOT EXISTS (
    SELECT 1 FROM service_items s WHERE s.slug = seed.slug
);

INSERT INTO project_items (
    slug, title, summary, description, client_industry, project_type,
    cover_image_url, live_url, language, status, featured, display_order,
    created_at, updated_at
)
SELECT * FROM (
    VALUES
    (
        'school-management-platform',
        'School Management Platform',
        'A full digital system for managing students, results, fees, parents, and staff.',
        'A production-ready school management platform with role-based dashboards, result publishing, parent access, fees, attendance, sessions, terms, and secure authentication.',
        'Education',
        'Web Application',
        'https://images.unsplash.com/photo-1523050854058-8df90110c9f1',
        '#',
        'EN',
        'PUBLISHED',
        TRUE,
        1,
        NOW(),
        NOW()
    ),
    (
        'agency-business-website',
        'Agency Business Website',
        'A polished company website with services, portfolio, products, clients, and admin content control.',
        'A modern company platform with multilingual support, dark mode, reusable sections, product blueprint pages, testimonials, lead capture, and admin-managed content.',
        'Professional Services',
        'Company Website',
        'https://images.unsplash.com/photo-1497366811353-6870744d04b2',
        '#',
        'EN',
        'PUBLISHED',
        TRUE,
        2,
        NOW(),
        NOW()
    ),
    (
        'booking-and-operations-system',
        'Booking & Operations System',
        'A workflow system for managing service bookings, customer records, and business operations.',
        'A tailored booking and operations platform built for businesses that need customer intake, booking requests, admin tracking, and operational visibility.',
        'Operations',
        'Business System',
        'https://images.unsplash.com/photo-1551434678-e076c223a692',
        '#',
        'EN',
        'PUBLISHED',
        TRUE,
        3,
        NOW(),
        NOW()
    )
) AS seed (
    slug, title, summary, description, client_industry, project_type,
    cover_image_url, live_url, language, status, featured, display_order,
    created_at, updated_at
)
WHERE NOT EXISTS (
    SELECT 1 FROM project_items p WHERE p.slug = seed.slug
);

INSERT INTO product_blueprints (
    slug, title, summary, challenge_statement, solution_overview,
    feature_highlights, hero_image_url, language, status, featured, display_order,
    created_at, updated_at
)
SELECT * FROM (
    VALUES
    (
        'school-management-blueprint',
        'School Management System Blueprint',
        'A complete school platform blueprint for administrators, teachers, parents, and students.',
        'Schools often manage results, payments, student data, attendance, and parent communication with disconnected tools.',
        'This blueprint provides one connected platform with secure access, dashboards, result publishing controls, printable reports, and structured school records.',
        'Student records, term results, session results, parent portal, fee tracking, attendance, admin dashboard, role-based access.',
        'https://images.unsplash.com/photo-1509062522246-3755977927d7',
        'EN',
        'PUBLISHED',
        TRUE,
        1,
        NOW(),
        NOW()
    ),
    (
        'service-business-blueprint',
        'Service Business Website Blueprint',
        'A high-converting service business platform with strong content, trust sections, and lead capture.',
        'Many businesses lose customers because their website does not explain their services clearly or create trust quickly.',
        'This blueprint structures services, portfolio, testimonials, contact forms, newsletter capture, and admin-managed website content.',
        'Hero section, services grid, portfolio, testimonials, client logos, CTA sections, contact forms, newsletter.',
        'https://images.unsplash.com/photo-1556761175-b413da4baf72',
        'EN',
        'PUBLISHED',
        TRUE,
        2,
        NOW(),
        NOW()
    )
) AS seed (
    slug, title, summary, challenge_statement, solution_overview,
    feature_highlights, hero_image_url, language, status, featured, display_order,
    created_at, updated_at
)
WHERE NOT EXISTS (
    SELECT 1 FROM product_blueprints p WHERE p.slug = seed.slug
);

INSERT INTO testimonials (
    client_name, client_role, organization, quote, avatar_url,
    language, status, featured, display_order,
    created_at, updated_at
)
SELECT * FROM (
    VALUES
    (
        'Ada Nwankwo',
        'Founder',
        'GrowthBridge Consult',
        'InFront helped us turn a rough idea into a clean digital platform. The structure, speed, and communication were excellent.',
        'https://images.unsplash.com/photo-1494790108377-be9c29b29330',
        'EN',
        'PUBLISHED',
        TRUE,
        1,
        NOW(),
        NOW()
    ),
    (
        'Chinedu Okafor',
        'Operations Lead',
        'Prime Logistics',
        'The platform made our booking process easier and gave our team a better way to manage customer requests.',
        'https://images.unsplash.com/photo-1500648767791-00dcc994a43e',
        'EN',
        'PUBLISHED',
        TRUE,
        2,
        NOW(),
        NOW()
    ),
    (
        'Mariam Bello',
        'Director',
        'BrightPath Academy',
        'The system brought order to our records, result workflow, and parent communication. It feels practical and built for real use.',
        'https://images.unsplash.com/photo-1531123897727-8f129e1688ce',
        'EN',
        'PUBLISHED',
        TRUE,
        3,
        NOW(),
        NOW()
    )
) AS seed (
    client_name, client_role, organization, quote, avatar_url,
    language, status, featured, display_order, created_at, updated_at
)
WHERE NOT EXISTS (
    SELECT 1 FROM testimonials t WHERE t.client_name = seed.client_name AND t.organization = seed.organization
);

INSERT INTO client_logos (
    name, logo_url, website_url, language, status, featured, display_order,
    created_at, updated_at
)
SELECT * FROM (
    VALUES
    (
        'GrowthBridge Consult',
        'https://dummyimage.com/240x90/111827/ffffff&text=GrowthBridge',
        'https://example.com',
        'EN',
        'PUBLISHED',
        TRUE,
        1,
        NOW(),
        NOW()
    ),
    (
        'Prime Logistics',
        'https://dummyimage.com/240x90/111827/ffffff&text=Prime+Logistics',
        'https://example.com',
        'EN',
        'PUBLISHED',
        TRUE,
        2,
        NOW(),
        NOW()
    ),
    (
        'BrightPath Academy',
        'https://dummyimage.com/240x90/111827/ffffff&text=BrightPath',
        'https://example.com',
        'EN',
        'PUBLISHED',
        TRUE,
        3,
        NOW(),
        NOW()
    ),
    (
        'Nexa Stores',
        'https://dummyimage.com/240x90/111827/ffffff&text=Nexa+Stores',
        'https://example.com',
        'EN',
        'PUBLISHED',
        TRUE,
        4,
        NOW(),
        NOW()
    )
) AS seed (
    name, logo_url, website_url, language, status, featured, display_order,
    created_at, updated_at
)
WHERE NOT EXISTS (
    SELECT 1 FROM client_logos c WHERE c.name = seed.name
);

INSERT INTO homepage_sections (
    section_key, title, subtitle, body, language, status, display_order,
    created_at, updated_at
)
SELECT * FROM (
    VALUES
    (
        'services',
        'Services designed for real business growth',
        'From websites to automation, we build digital systems that support your next stage.',
        'Explore flexible services for launching online, improving operations, and building better customer experiences.',
        'EN',
        'PUBLISHED',
        1,
        NOW(),
        NOW()
    ),
    (
        'portfolio',
        'Recent work and practical solutions',
        'See examples of platforms, systems, and product structures built for different business needs.',
        'Our work focuses on clarity, usability, performance, and business value.',
        'EN',
        'PUBLISHED',
        2,
        NOW(),
        NOW()
    ),
    (
        'products',
        'Blueprints for faster launches',
        'Use proven product structures to plan your next platform, business website, or internal system.',
        'Blueprints help you move from idea to implementation with less confusion.',
        'EN',
        'PUBLISHED',
        3,
        NOW(),
        NOW()
    ),
    (
        'trust',
        'Trusted by growing businesses',
        'We work with businesses that want clean systems, strong execution, and long-term value.',
        'Our approach combines strategy, design, development, and practical support.',
        'EN',
        'PUBLISHED',
        4,
        NOW(),
        NOW()
    )
) AS seed (
    section_key, title, subtitle, body, language, status, display_order,
    created_at, updated_at
)
WHERE NOT EXISTS (
    SELECT 1 FROM homepage_sections h WHERE h.section_key = seed.section_key AND h.language = seed.language
);