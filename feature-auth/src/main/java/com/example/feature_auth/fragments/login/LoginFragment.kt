package com.example.feature_auth.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.core.util.Constants.Companion.MAIN_ACTIVITY_PATH
import com.example.feature_auth.R
import com.example.feature_auth.databinding.FragmentLoginBinding
import com.example.feature_auth.utils.navigateToActivity
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tietEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.setUsername(text.toString())
        }

        binding.tietPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.setPassword(text.toString())
        }

        binding.mbtLogin.setOnClickListener {
            if(viewModel.checkLoginData()){
                viewModel.singIn()
                observeLoginStatus()
            } else {
                Toast.makeText(requireContext(), "Incorrect e-mail or password.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        binding.mbtRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragmentNav_to_registrationFragmentNav)
        }
    }

    private fun observeLoginStatus(){
        loginJob = lifecycleScope.launch {
            viewModel.isLoginSuccesfull.collectLatest {result ->
                if(result){
                    requireContext().navigateToActivity(MAIN_ACTIVITY_PATH)
                } else{
                    Toast.makeText(requireContext(), "Login failed - check your e-mail and password.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStop() {
        loginJob?.cancel()
        super.onStop()
    }
}