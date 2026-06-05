package com.clothing.crm.config;

import com.clothing.crm.entity.Product;
import com.clothing.crm.entity.User;
import com.clothing.crm.repository.ProductRepository;
import com.clothing.crm.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UserRepository userRepository, ProductRepository productRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Users
        if (userRepository.count() == 0) {
            User admin = new User(
                    "admin@clothing.com",
                    passwordEncoder.encode("admin123"),
                    "Admin Administrator",
                    "ROLE_ADMIN"
            );
            User customer = new User(
                    "customer@clothing.com",
                    passwordEncoder.encode("customer123"),
                    "Demo Customer LLC",
                    "ROLE_USER"
            );
            userRepository.saveAll(Arrays.asList(admin, customer));
            System.out.println("Demo users seeded successfully: admin@clothing.com / customer@clothing.com");
        }

        // 2. Seed 30 Products
        if (productRepository.count() == 0) {
            Product[] products = new Product[]{
                // T-Shirts
                new Product("Classic White Crewneck T-Shirt", "TSH-WHT-01", 12.0, 250, "T-Shirts", 
                        "High-quality 100% premium cotton white t-shirt. Breathable, durable, and standard fit.", 
                        "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=500"),
                new Product("Heavyweight Black Cotton Tee", "TSH-BLK-02", 14.5, 300, "T-Shirts", 
                        "Thick heavyweight organic cotton black t-shirt. Perfect for casual streetwear styling.", 
                        "https://images.unsplash.com/photo-1503342217505-b0a15ec3261c?w=500"),
                new Product("Vintage Graphic Print T-Shirt", "TSH-GRA-03", 16.0, 180, "T-Shirts", 
                        "Soft-wash retro graphic tee featuring a vintage screen-printed design on chest.", 
                        "https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=500"),
                new Product("Slim Fit Navy Polo Shirt", "POL-NAV-04", 18.0, 150, "T-Shirts", 
                        "Pique knit cotton polo shirt in deep navy. Ribbed collar and classic double-button placket.", 
                        "https://images.unsplash.com/photo-1581655353564-df123a1eb820?w=500"),
                new Product("Striped Summer Tee", "TSH-STR-05", 13.0, 220, "T-Shirts", 
                        "Lightweight horizontal striped cotton t-shirt. Excellent choice for beach and summer wear.", 
                        "https://images.unsplash.com/photo-1562157873-818bc0726f68?w=500"),

                // Shirts
                new Product("Classic Oxford Blue Shirt", "SHI-OXF-06", 28.0, 140, "Shirts", 
                        "Traditional Oxford cotton long-sleeve dress shirt in light blue. Features button-down collar.", 
                        "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=500"),
                new Product("Flannel Plaid Casual Shirt", "SHI-FLA-07", 24.0, 200, "Shirts", 
                        "Warm cotton flannel shirt with red and black plaid checkers. Ideal for layering in autumn.", 
                        "https://images.unsplash.com/photo-1598033129183-c4f50c736f10?w=500"),
                new Product("White Formal Dress Shirt", "SHI-FRM-08", 32.0, 100, "Shirts", 
                        "Premium cotton twill tuxedo-ready white shirt. Features double French cuffs for cufflinks.", 
                        "https://images.unsplash.com/photo-1620012253295-c05518e99309?w=500"),
                new Product("Linen Casual Beach Shirt", "SHI-LIN-09", 26.5, 120, "Shirts", 
                        "Breathable lightweight pure linen casual shirt. Perfect for tropical hot summer days.", 
                        "https://images.unsplash.com/photo-1603252109303-2751441dd157?w=500"),
                new Product("Slim Fit Denim Shirt", "SHI-DEN-10", 30.0, 90, "Shirts", 
                        "Sturdy washed indigo denim shirt with double chest snap button pockets.", 
                        "https://images.unsplash.com/photo-1588359348347-9bc6cbaa689f?w=500"),

                // Pants
                new Product("Dark Indigo Slim Fit Jeans", "JNS-IND-11", 35.0, 210, "Pants", 
                        "Classic blue indigo stretchable denim jeans. Slim fit with contrast amber stitching.", 
                        "https://images.unsplash.com/photo-1542272604-787c3835535d?w=500"),
                new Product("Black Distressed Denim Jeans", "JNS-BLK-12", 38.0, 160, "Pants", 
                        "Faded black denim jeans featuring ripped knees and worn-in edges for an edgy style.", 
                        "https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=500"),
                new Product("Khaki Chino Pants", "PAN-KHA-13", 29.0, 175, "Pants", 
                        "Stretch-cotton flat-front chino trousers in classic tan khaki color. Smart-casual look.", 
                        "https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?w=500"),
                new Product("Charcoal Grey Tailored Trousers", "PAN-TR-14", 42.0, 80, "Pants", 
                        "Slim-fitting tailored formal trousers in dark charcoal wool-blend fabric.", 
                        "https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=500"),
                new Product("Athletic Jogger Pants", "PAN-JOG-15", 22.0, 240, "Pants", 
                        "Heavy fleece athletic sweatpants with elastic drawstring waistband and cuffed ankles.", 
                        "https://images.unsplash.com/photo-1551854838-212c50b4c184?w=500"),

                // Outerwear
                new Product("Waterproof Hooded Rain Jacket", "JKT-RAIN-16", 55.0, 95, "Outerwear", 
                        "Lightweight windproof and waterproof yellow raincoat. Ideal for hiking and stormy weather.", 
                        "https://images.unsplash.com/photo-1551488831-00ddcb6c6bd3?w=500"),
                new Product("Classic Camel Wool Overcoat", "JKT-CAM-17", 110.0, 45, "Outerwear", 
                        "Luxury camel wool-blend double-breasted long winter coat. Keeps warm with elegant silhouette.", 
                        "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=500"),
                new Product("Urban Bomber Jacket", "JKT-BOM-18", 48.0, 110, "Outerwear", 
                        "Sleek black nylon flight bomber jacket with orange inner lining and zippered arm pocket.", 
                        "https://images.unsplash.com/photo-1548883354-7622d03aca27?w=500"),
                new Product("Knitted Cable Crewneck Sweater", "SWE-CAB-19", 34.0, 130, "Outerwear", 
                        "Thick cotton-blend cable knit sweater in off-white cream color. Soft, cozy and warm.", 
                        "https://images.unsplash.com/photo-1620799140408-edc6dcb6d633?w=500"),
                new Product("Sherpa Lined Corduroy Jacket", "JKT-SHR-20", 58.0, 70, "Outerwear", 
                        "Retro brown corduroy trucker jacket fully lined with warm synthetic sherpa fleece.", 
                        "https://images.unsplash.com/photo-1617137968427-85924c800a22?w=500"),

                // Suits
                new Product("Premium Navy Wool Business Suit", "SUT-NAV-21", 175.0, 35, "Suits", 
                        "Tailored fit navy blue wool two-piece suit. Includes lined single-breasted blazer and trousers.", 
                        "https://images.unsplash.com/photo-1594938298603-c8148c4dae35?w=500"),
                new Product("Charcoal Grey Double-Breasted Suit", "SUT-CHR-22", 190.0, 25, "Suits", 
                        "Elegant peak lapel double-breasted suit in structured charcoal wool blend. Highly formal.", 
                        "https://images.unsplash.com/photo-1593030761757-71fae45fa0e7?w=500"),
                new Product("Classic Tweed Blazer", "BLZ-TWD-23", 85.0, 60, "Suits", 
                        "Sophisticated brown elbow-patched tweed blazer. Great for academic and ivy league styles.", 
                        "https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=500"),
                new Product("Slim Fit Tuxedo Set", "SUT-TUX-24", 220.0, 15, "Suits", 
                        "Ultra-premium black wool tuxedo with satin shawl collar, matching trousers and silk bowtie.", 
                        "https://images.unsplash.com/photo-1598808503746-f34c53b20ef3?w=500"),
                new Product("Casual Linen Blazer", "BLZ-LIN-25", 78.0, 50, "Suits", 
                        "Relaxed fit beige linen blazer. Unlined for maximum breathability in warm weather.", 
                        "https://images.unsplash.com/photo-1555069513-0473a57704b2?w=500"),

                // Accessories
                new Product("Genuine Leather Belt Black", "ACC-BEL-26", 12.0, 350, "Accessories", 
                        "100% full-grain cowhide leather belt in deep black. Solid steel polished metal buckle.", 
                        "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500"),
                new Product("Polarized Classic Sunglasses", "ACC-SUN-27", 15.0, 200, "Accessories", 
                        "Retro horn-rimmed black sunglasses with UV400 protective dark grey polarized lenses.", 
                        "https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=500"),
                new Product("Minimalist Quartz Wristwatch", "ACC-WAT-28", 45.0, 120, "Accessories", 
                        "Elegant black dial watch with silver mesh stainless steel band. Water resistant to 30m.", 
                        "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500"),
                new Product("Wool Blend Winter Scarf", "ACC-SCF-29", 10.0, 280, "Accessories", 
                        "Soft wool blend unisex scarf in checkered forest green and grey stripes.", 
                        "https://images.unsplash.com/photo-1520639888713-7851133b1ed0?w=500"),
                new Product("Classic Canvas Backpack", "ACC-BPK-30", 25.0, 160, "Accessories", 
                        "Durable water-resistant cotton canvas school and travel bag with leather buckle details.", 
                        "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500")
            };

            productRepository.saveAll(Arrays.asList(products));
            System.out.println("30 Demo products seeded successfully.");
        }
    }
}
