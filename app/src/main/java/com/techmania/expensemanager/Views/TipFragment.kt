package com.techmania.expensemanager.Views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.techmania.expensemanager.Adapters.TipsAdapter
import com.techmania.expensemanager.databinding.LayoutTipsBinding

class TipFragment : Fragment() {

    private lateinit var binding: LayoutTipsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutTipsBinding.inflate(inflater, container, false)

        setUpAdapter()

        return binding.root
    }

    private fun setUpAdapter() {
        val titles = listOf(
            "Set Budget Goals",
            "Save for Emergencies",
            "Limit Impulse Purchases",
            "Utilize Discounts and Coupons",
            "Automate Savings",
            "Review Your Bills",
            "Plan for Large Expenses",
            "Monitor Your Credit Score",
            "Invest Wisely",
            "Seek Financial Advice",
            "Celebrate Milestones"
        )

        val descriptions = listOf(
            "Establish realistic budget goals for different categories such as groceries, dining out, entertainment, etc. Having clear targets can help you stay on track and manage your finances more effectively.",
            "Allocate a portion of your income towards an emergency fund. Aim to build up enough savings to cover at least three to six months’ worth of living expenses to cushion against unexpected financial setbacks.",
            "Before making a non-essential purchase, give yourself some time to think it over. Ask yourself if it aligns with your financial goals and if it's something you truly need.",
            "Take advantage of discounts, coupons, and cashback offers whenever possible. This can help you save money on everyday purchases and stretch your budget further.",
            "Set up automatic transfers to your savings account or investment accounts each payday. This way, you're less likely to spend the money before saving it.",
            "Periodically review your recurring bills and subscriptions to see if there are any services you no longer use or can negotiate for a better rate.",
            "Anticipate big expenses such as vacations, home repairs, or vehicle maintenance and start saving for them in advance. This can help prevent financial strain when these costs arise.",
            "Regularly check your credit score and report to ensure there are no errors and to track your financial health. A good credit score can save you money on loans and insurance premiums.",
            "Consider investing for the long term to grow your wealth. Research different investment options such as stocks, bonds, mutual funds, or real estate, and choose ones that align with your risk tolerance and financial goals.",
            "If you're unsure about managing your finances or planning for the future, don't hesitate to seek advice from a financial advisor or use reputable online resources to educate yourself further.",
            "Acknowledge your financial achievements, whether it's paying off debt, reaching a savings goal, or sticking to your budget consistently. Celebrating milestones can help motivate you to continue making progress."
        )

        val adapter = TipsAdapter(requireContext(),titles,descriptions)
        binding.rvTips.adapter = adapter
    }

}