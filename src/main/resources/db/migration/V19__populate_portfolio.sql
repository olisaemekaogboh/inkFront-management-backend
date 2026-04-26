-- Clear existing portfolio items
DELETE FROM project_items;

INSERT INTO project_items (
    title, slug, summary, description, client_industry, project_type,
    cover_image_url, live_url, language, status, featured, display_order, created_at, updated_at
)
VALUES

-- 01. EduBridge School Platform
(
    'EduBridge School Management Platform',
    'edubridge-school-platform',
    'Complete school administration system serving 2,000+ students across 3 campuses.',
    'EduBridge needed to replace their paper-based system with a digital platform. We built a comprehensive school management solution with student enrollment, attendance tracking, grade management, fee collection, parent portal, and staff administration. The platform now manages 2,000+ students across 3 campuses with real-time reporting.',
    'Education',
    'Web Application',
    'https://images.unsplash.com/photo-1523050854058-8df90110c9f1?auto=format&fit=crop&w=800&q=80',
    '#',
    'EN', 'PUBLISHED', true, 1, NOW(), NOW()
),

-- 02. QuickShip Logistics Dashboard
(
    'QuickShip Logistics Dashboard',
    'quickship-logistics-dashboard',
    'Real-time dispatch and tracking system for 150+ delivery riders.',
    'QuickShip was struggling with manual dispatch that caused delivery delays. We designed a logistics dashboard with live rider tracking, automated dispatch, route optimization, and proof-of-delivery capture. The system reduced delivery time by 35% in the first month.',
    'Logistics',
    'Dashboard',
    'https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?auto=format&fit=crop&w=800&q=80',
    '#',
    'EN', 'PUBLISHED', true, 2, NOW(), NOW()
),

-- 03. HalaMart Multi-Vendor Marketplace
(
    'HalaMart Multi-Vendor Marketplace',
    'halamart-marketplace',
    'Multi-vendor e-commerce platform with escrow payments and vendor analytics.',
    'HalaMart wanted to connect local vendors with buyers in a trusted marketplace. We built a multi-vendor platform with vendor onboarding, product catalogs, escrow payment protection, rating systems, and performance dashboards. The platform launched with 50+ vendors and processed $100K in transactions in the first quarter.',
    'E-commerce',
    'Marketplace',
    'https://images.unsplash.com/photo-1518458028785-8fbcd101ebb9?auto=format&fit=crop&w=800&q=80',
    '#',
    'EN', 'PUBLISHED', true, 3, NOW(), NOW()
),

-- 04. PaySwift Bill Payments App
(
    'PaySwift Bill Payments App',
    'payswift-bill-payments',
    'Mobile bill payment platform processing airtime, data, and utility payments.',
    'PaySwift needed a reliable platform for processing recurring bill payments. We developed a mobile-first application with multi-service payment integration, wallet system, transaction history, and auto-renewal features. The app now processes 10,000+ monthly transactions.',
    'Fintech',
    'Mobile App',
    'https://images.unsplash.com/photo-1554224155-6726b3ff858f?auto=format&fit=crop&w=800&q=80',
    '#',
    'EN', 'PUBLISHED', true, 4, NOW(), NOW()
),

-- 05. PropertyFinder Real Estate
(
    'PropertyFinder Real Estate Marketplace',
    'propertyfinder-real-estate',
    'Property listing platform connecting agents, buyers, and renters.',
    'PropertyFinder wanted to digitize the property search experience. We created a real estate marketplace with advanced search filters, map integration, agent profiles, inquiry management, and virtual tour support. The platform lists 500+ properties and handles 200+ daily inquiries.',
    'Real Estate',
    'Web Platform',
    'https://images.unsplash.com/photo-1560518883-ce09059eeffa?auto=format&fit=crop&w=800&q=80',
    '#',
    'EN', 'PUBLISHED', true, 5, NOW(), NOW()
),

-- 06. CryptoTrust P2P Exchange
(
    'CryptoTrust P2P Exchange',
    'cryptotrust-p2p-exchange',
    'Peer-to-peer crypto trading platform with escrow and dispute resolution.',
    'CryptoTrust needed a secure P2P exchange for their growing user base. We built a platform with multi-currency support, escrow smart contracts, KYC/AML compliance, real-time order matching, and automated dispute resolution. The exchange handles $2M+ in monthly volume.',
    'Crypto / Web3',
    'Platform',
    'https://images.unsplash.com/photo-1639762681485-074b7f938ba0?auto=format&fit=crop&w=800&q=80',
    '#',
    'EN', 'PUBLISHED', false, 6, NOW(), NOW()
),

-- 07. StyleMatch Dating App
(
    'StyleMatch Dating Platform',
    'stylematch-dating-app',
    'Modern dating app with AI matching and video chat features.',
    'StyleMatch wanted to stand out in the dating space with better matching. We developed a dating platform with AI-powered matching algorithms, real-time chat, video calls, subscription tiers, and moderation tools. The app gained 50K users within 3 months of launch.',
    'Social / Dating',
    'Mobile App',
    'https://images.unsplash.com/photo-1529333166437-7750a6dd5a70?auto=format&fit=crop&w=800&q=80',
    '#',
    'EN', 'PUBLISHED', false, 7, NOW(), NOW()
),

-- 08. BloomMusic Streaming
(
    'BloomMusic Streaming Platform',
    'bloommusic-streaming',
    'Music streaming and distribution platform for independent artists.',
    'BloomMusic wanted to help independent artists distribute and monetize music. We built a streaming platform with music upload, playlist creation, royalty tracking, artist profiles, and listener analytics. The platform hosts 1,000+ artists and serves 100K+ monthly listeners.',
    'Entertainment',
    'Web & Mobile',
    'https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=800&q=80',
    '#',
    'EN', 'PUBLISHED', true, 8, NOW(), NOW()
),

-- 09. LoanEase Digital Lending
(
    'LoanEase Digital Lending Platform',
    'loanease-digital-lending',
    'Automated loan origination and management system.',
    'LoanEase needed to move from paper-based lending to digital. We built a loan platform with automated credit scoring, digital KYC, multi-tier approvals, flexible repayment scheduling, and collection tools. Loan processing time dropped from 2 weeks to 2 hours.',
    'Fintech',
    'Platform',
    'https://images.unsplash.com/photo-1579621970563-ebec7560ff3e?auto=format&fit=crop&w=800&q=80',
    '#',
    'EN', 'PUBLISHED', false, 9, NOW(), NOW()
),

-- 10. MediCare Facility Management
(
    'MediCare Facility Management System',
    'medicare-facility-management',
    'Hospital facility and maintenance management platform.',
    'MediCare was struggling with manual facility maintenance across 3 hospital locations. We developed a system with asset tracking, preventive maintenance scheduling, work order management, vendor management, and compliance reporting. Equipment downtime reduced by 40%.',
    'Healthcare',
    'Enterprise Software',
    'https://images.unsplash.com/photo-1581092160562-40aa08e78837?auto=format&fit=crop&w=800&q=80',
    '#',
    'EN', 'PUBLISHED', true, 10, NOW(), NOW()
),

-- 11. AdReach Media Buyer Platform
(
    'AdReach Media Buyer Platform',
    'adreach-media-buyer',
    'Cross-channel ad campaign management and reporting system.',
    'AdReach needed a unified platform for managing ad campaigns across channels. We built a media buying platform with campaign scheduling, budget optimization, performance analytics, and white-label client reporting. The platform manages $500K+ in monthly ad spend.',
    'Advertising',
    'SaaS Platform',
    'https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=800&q=80',
    '#',
    'EN', 'PUBLISHED', false, 11, NOW(), NOW()
),

-- 12. SaveWise Investment Platform
(
    'SaveWise Savings & Investment',
    'savewise-investment-platform',
    'Digital wealth management with savings plans and investment tracking.',
    'SaveWise wanted to make saving and investing accessible to everyday users. We created a platform with goal-based savings plans, automated debits, investment portfolio tracking, returns calculators, and financial literacy content. The app has helped 5,000+ users save over $2M.',
    'Fintech',
    'Mobile App',
    'https://images.unsplash.com/photo-1554224155-6726b3ff858f?auto=format&fit=crop&w=800&q=80',
    '#',
    'EN', 'PUBLISHED', true, 12, NOW(), NOW()
);