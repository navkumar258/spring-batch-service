import csv
import random
import os

def generate_customer_data(num_records=10000):
    """Generates a CSV file with dummy customer data."""

    # Define the output file path
    # Assuming you want it in the same directory as your Spring Boot project's resources
    script_dir = os.path.dirname(__file__)
    output_path = os.path.join(script_dir, 'src', 'main', 'resources', 'customers.csv')

    # Ensure the directory exists
    os.makedirs(os.path.dirname(output_path), exist_ok=True)

    headers = ["id", "firstName", "lastName", "email", "phone", "loyaltyTier"]
    first_names = ["John", "Jane", "Peter", "Alice", "Bob", "Charlie", "Diana", "Eve", "Frank", "Grace"]
    last_names = ["Doe", "Smith", "Jones", "Brown", "White", "Black", "Green", "Hall", "King", "Lee"]
    domains = ["example.com", "mail.com", "domain.org", "service.net"]
    loyalty_tiers = ["Gold", "Silver", "Bronze", "Platinum"]

    print(f"Generating {num_records} customer records to: {output_path}")

    with open(output_path, 'w', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(headers) # Write header row

        for i in range(1, num_records + 1):
            first_name = random.choice(first_names)
            last_name = random.choice(last_names)
            email_prefix = f"{first_name.lower()}.{last_name.lower()}{i}"
            email = f"{email_prefix}@{random.choice(domains)}"
            phone = f"{random.randint(100, 999)}-{random.randint(100, 999)}-{random.randint(1000, 9999)}"
            loyalty_tier = random.choice(loyalty_tiers)

            # Introduce some invalid data for testing fault tolerance
            if i % 100 == 0: # Every 100th record gets an invalid email
                email = "invalid-email"
            if i % 150 == 0: # Every 150th record gets a missing phone
                phone = ""
            if i % 200 == 0: # Every 200th record gets a completely unknown loyalty tier
                loyalty_tier = "UnknownTier"

            writer.writerow([i, first_name, last_name, email, phone, loyalty_tier])

    print("CSV generation complete.")

if __name__ == "__main__":
    generate_customer_data(10000)