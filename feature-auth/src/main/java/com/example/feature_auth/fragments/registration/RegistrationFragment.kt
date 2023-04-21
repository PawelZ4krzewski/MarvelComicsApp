package com.example.feature_auth.fragments.registration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.feature_auth.R
import com.example.feature_auth.databinding.FragmentRegistrationBinding
import com.example.feature_auth.fragments.login.LoginViewModel
import com.example.feature_main.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: RegistrationViewModel by viewModels()

    private var registrationJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.setUsername(text.toString())
        }

        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.setPassword(text.toString())
        }

        binding.etRepeatPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.setRepeatPassword(text.toString())
        }

        binding.btRegistration.setOnClickListener {
            viewModel.createUser()
            observeRegistrationStatus()
        }


    }

    private fun observeRegistrationStatus(){
        registrationJob = lifecycleScope.launch {
            viewModel.isRegistrationSuccesfull.collectLatest {result ->
                if(result){
                    startApp()
                } else{
                    Toast.makeText(requireContext(), "Registration failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startApp(){
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }

    override fun onStop() {
        registrationJob?.cancel()
        super.onStop()
    }

}