INSERT INTO product_blueprints (
    title,
    slug,
    summary,
    challenge_statement,
    solution_overview,
    feature_highlights,
    hero_image_url,
    language,
    status,
    featured,
    display_order,
    created_at,
    updated_at
)
VALUES

(
'Business Website Blueprint',
'business-website-blueprint',
'A premium company website structure for service businesses.',
'Poor structure and no trust online.',
'Complete conversion-focused company website.',
'Hero, services, portfolio, testimonials, CTA.',
'https://images.unsplash.com/photo-1497366754035-f200968a6e72?auto=format&fit=crop&w=1400&q=80',
'EN',
'PUBLISHED',
true,
1,
NOW(),
NOW()
),

(
'Booking Platform Blueprint',
'booking-platform-blueprint',
'A complete booking platform.',
'Manual booking problems.',
'Online scheduling system.',
'Calendar, dashboard, notifications.',
'https://images.unsplash.com/photo-1554224155-6726b3ff858f?auto=format&fit=crop&w=1400&q=80',
'EN',
'PUBLISHED',
true,
2,
NOW(),
NOW()
),

(
'Client Portal Blueprint',
'client-portal-blueprint',
'Secure client dashboard.',
'Scattered communication.',
'Unified portal.',
'Login, requests, progress tracking.',
'https://images.unsplash.com/photo-1551434678-e076c223a692?auto=format&fit=crop&w=1400&q=80',
'EN',
'PUBLISHED',
true,
3,
NOW(),
NOW()
),

(
'School Management Blueprint',
'school-management-blueprint',
'Complete school system.',
'Manual school processes.',
'Digitized management system.',
'Results, fees, attendance.',
'https://images.unsplash.com/photo-1523050854058-8df90110c9f1?auto=format&fit=crop&w=1400&q=80',
'EN',
'PUBLISHED',
true,
4,
NOW(),
NOW()
),

(
'E-Commerce Blueprint',
'e-commerce-blueprint',
'Modern ecommerce system.',
'Selling only on social media.',
'Full store solution.',
'Cart, checkout, orders.',
'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?auto=format&fit=crop&w=1400&q=80',
'EN',
'PUBLISHED',
false,
5,
NOW(),
NOW()
)

ON CONFLICT (slug)
DO UPDATE SET
title = EXCLUDED.title,
summary = EXCLUDED.summary,
challenge_statement = EXCLUDED.challenge_statement,
solution_overview = EXCLUDED.solution_overview,
feature_highlights = EXCLUDED.feature_highlights,
hero_image_url = EXCLUDED.hero_image_url,
featured = EXCLUDED.featured,
display_order = EXCLUDED.display_order,
updated_at = NOW();