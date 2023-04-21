package com.example.feature_auth.fragments.login

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.feature_auth.R
import com.example.feature_auth.databinding.FragmentLoginBinding
import com.example.feature_main.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    private var loginJob: Job? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.setUsername(text.toString())
        }

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.setPassword(text.toString())
        }

        binding.btLogin.setOnClickListener {
            viewModel.singIn()
            observeLoginStatus()
        }

        binding.btRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragmentNav_to_registrationFragmentNav)
        }
    }

    private fun observeLoginStatus(){
        loginJob = lifecycleScope.launch {
            viewModel.isLoginSuccesfull.collectLatest {result ->
                if(result){
                    startApp()
                } else{
                    Toast.makeText(requireContext(), "Login failed.",
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
        loginJob?.cancel()
        super.onStop()
    }
}