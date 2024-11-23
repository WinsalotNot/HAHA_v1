package com.example.servigo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedRecyclerViewModel : ViewModel() {
    private val _rankingList = MutableLiveData<List<RankingData>>()
    val rankingList: LiveData<List<RankingData>> get() = _rankingList

    init {
        _rankingList.value = listOf(
            // Existing Entries
            RankingData(
                name = "Dewi Lestari",
                title = "Electrical Expert",
                description = "Specialist in electrical installation and troubleshooting with over 8 years of experience.",
                rank = "S",
                rating = 4.9f,
                review = 140,
                addr = "Jl. Sudirman, Jakarta Selatan",
                fee = 600000,
                img = "boo",
                cat = "Electrical",
                shortDesc = "Experienced and certified electrical technician."
            ),
            RankingData(
                name = "Ahmad Santoso",
                title = "Plumbing Specialist",
                description = "Experienced plumber handling all types of water system repairs and installations.",
                rank = "A",
                rating = 4.7f,
                review = 110,
                addr = "Jl. Mangga Dua, Jakarta Barat",
                fee = 450000,
                img = "boo",
                cat = "Plumbing",
                shortDesc = "Reliable plumber with expertise in water systems."
            ),
            RankingData(
                name = "Siti Nurhaliza",
                title = "Home Cleaning Expert",
                description = "Dedicated cleaning professional providing spotless results for residential spaces.",
                rank = "B",
                rating = 4.3f,
                review = 90,
                addr = "Jl. Gatot Subroto, Jakarta Timur",
                fee = 350000,
                img = "boo",
                cat = "Cleaning",
                shortDesc = "Efficient and detailed home cleaning services."
            ),
            RankingData(
                name = "Budi Prasetyo",
                title = "Skilled Plumber",
                description = "Trusted professional in plumbing repairs and water leak solutions.",
                rank = "C",
                rating = 4.1f,
                review = 75,
                addr = "Jl. Kelapa Gading, Jakarta Utara",
                fee = 400000,
                img = "boo",
                cat = "Plumbing",
                shortDesc = "Effective plumbing solutions for your home or office."
            ),
            RankingData(
                name = "Mega Putri",
                title = "Electrical Troubleshooter",
                description = "Efficient in diagnosing and fixing electrical issues in record time.",
                rank = "D",
                rating = 3.8f,
                review = 60,
                addr = "Jl. Thamrin, Jakarta Pusat",
                fee = 300000,
                img = "boo",
                cat = "Electrical",
                shortDesc = "Swift and reliable electrical troubleshooting."
            ),
            RankingData(
                name = "Rizki Ramadhan",
                title = "Cleaning Professional",
                description = "Top-notch cleaning expert ensuring satisfaction in every job.",
                rank = "E",
                rating = 3.5f,
                review = 50,
                addr = "Jl. Tebet, Jakarta Selatan",
                fee = 250000,
                img = "boo",
                cat = "Cleaning",
                shortDesc = "Affordable and reliable cleaning services."
            ),
            // New Entries
            RankingData(
                name = "Yulia Kartika",
                title = "Plumbing Technician",
                description = "Focused plumber delivering reliable and timely services.",
                rank = "S",
                rating = 4.8f,
                review = 120,
                addr = "Jl. Tanjung Duren, Jakarta Barat",
                fee = 500000,
                img = "boo",
                cat = "Plumbing",
                shortDesc = "Expert plumbing services for all your needs."
            ),
            RankingData(
                name = "Eko Saputra",
                title = "Electrician",
                description = "Certified electrician with experience in complex installations.",
                rank = "A",
                rating = 4.6f,
                review = 95,
                addr = "Jl. Cempaka Putih, Jakarta Pusat",
                fee = 550000,
                img = "boo",
                cat = "Electrical",
                shortDesc = "Professional electrical services with safety assurance."
            ),
            RankingData(
                name = "Andi Wirawan",
                title = "Residential Cleaner",
                description = "Provides premium cleaning for homes and apartments.",
                rank = "B",
                rating = 4.2f,
                review = 80,
                addr = "Jl. Pondok Indah, Jakarta Selatan",
                fee = 320000,
                img = "boo",
                cat = "Cleaning",
                shortDesc = "Efficient and friendly cleaning services."
            ),
            RankingData(
                name = "Putri Sari",
                title = "Plumbing Technician",
                description = "Experienced in modern plumbing solutions and repairs.",
                rank = "C",
                rating = 4.0f,
                review = 65,
                addr = "Jl. Sunter, Jakarta Utara",
                fee = 380000,
                img = "boo",
                cat = "Plumbing",
                shortDesc = "Affordable and dependable plumbing services."
            ),
            RankingData(
                name = "Indah Permata",
                title = "Home Cleaner",
                description = "Skilled in deep cleaning and ensuring a fresh environment.",
                rank = "D",
                rating = 3.7f,
                review = 45,
                addr = "Jl. Rasuna Said, Jakarta Timur",
                fee = 280000,
                img = "boo",
                cat = "Cleaning",
                shortDesc = "Detail-oriented home cleaning services."
            ),
            RankingData(
                name = "Rian Pratama",
                title = "Certified Electrician",
                description = "Handles residential and commercial electrical systems.",
                rank = "E",
                rating = 3.4f,
                review = 40,
                addr = "Jl. Meruya, Jakarta Barat",
                fee = 260000,
                img = "boo",
                cat = "Electrical",
                shortDesc = "Cost-effective electrical solutions for your property."
            ),
            RankingData(
                name = "Fajar Nugroho",
                title = "Master Plumber",
                description = "Specialist in large-scale plumbing projects and repairs.",
                rank = "S",
                rating = 4.9f,
                review = 150,
                addr = "Jl. Kemang, Jakarta Selatan",
                fee = 700000,
                img = "boo",
                cat = "Plumbing",
                shortDesc = "Top-rated plumbing services for modern homes."
            ),
            RankingData(
                name = "Tari Lestari",
                title = "Detail Cleaner",
                description = "Specialist in cleaning hard-to-reach spaces with modern tools.",
                rank = "A",
                rating = 4.5f,
                review = 100,
                addr = "Jl. Pasar Minggu, Jakarta Timur",
                fee = 480000,
                img = "boo",
                cat = "Cleaning",
                shortDesc = "Deep cleaning with guaranteed satisfaction."
            ),
            RankingData(
                name = "Adi Santoso",
                title = "Electrician & Installer",
                description = "Provides expert electrical installations and emergency fixes.",
                rank = "B",
                rating = 4.3f,
                review = 85,
                addr = "Jl. Palmerah, Jakarta Barat",
                fee = 420000,
                img = "boo",
                cat = "Electrical",
                shortDesc = "Reliable electrical services with quick turnaround."
            ),
            RankingData(
                name = "Nurhayati Malik",
                title = "Plumbing Expert",
                description = "Solves water system issues efficiently and affordably.",
                rank = "C",
                rating = 4.1f,
                review = 70,
                addr = "Jl. Pademangan, Jakarta Utara",
                fee = 360000,
                img = "boo",
                cat = "Plumbing",
                shortDesc = "Experienced in water pipe maintenance and repair."
            ),
            RankingData(
                name = "Deni Kurniawan",
                title = "Residential Electrician",
                description = "Focused on safe and efficient electrical repairs.",
                rank = "D",
                rating = 3.6f,
                review = 55,
                addr = "Jl. Grogol, Jakarta Barat",
                fee = 300000,
                img = "boo",
                cat = "Electrical",
                shortDesc = "Affordable and reliable electrician for home needs."
            ),
            RankingData(
                name = "Lia Marlina",
                title = "Quick Cleaner",
                description = "Provides fast and efficient cleaning services.",
                rank = "E",
                rating = 3.3f,
                review = 30,
                addr = "Jl. Cipete, Jakarta Selatan",
                fee = 200000,
                img = "boo",
                cat = "Cleaning",
                shortDesc = "Quick and affordable cleaning services."
            )
        )
    }

}
