package com.madarsoft.userinfoapp.ui.userdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.madarsoft.userinfoapp.R
import com.madarsoft.userinfoapp.data.User
import com.madarsoft.userinfoapp.databinding.FragmentUserDetailsBinding

class UserDetailsFragment : Fragment(R.layout.fragment_user_details) {

    private val args: UserDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentUserDetailsBinding.bind(view)

        val createdUser = args.user

        updateUIWithUserDetails(binding, createdUser)
    }

    private fun updateUIWithUserDetails(binding: FragmentUserDetailsBinding, createdUser: User) {
        binding.apply {
            userName.text = "${resources.getString(R.string.name)} ${createdUser.name}"
            userAge.text = "${resources.getString(R.string.age)} ${createdUser.age}"
            userJobTitle.text = "${resources.getString(R.string.job_title)} ${createdUser.jobTitle}"

            val selectedGender = createdUser.gender

            userGender.text = "${resources.getString(R.string.gender)} $selectedGender"

            var genderIcon: Int =
                if (selectedGender == resources.getString(R.string.male)) R.drawable.ic_baseline_boy_24
                else R.drawable.ic_baseline_girl_24

            userGender.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, genderIcon, 0)
        }
    }
}