package com.example.feature_auth.fragments.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.core.util.Constants.Companion.MAIN_ACTIVITY_PATH
import com.example.feature_auth.R
import com.example.feature_auth.databinding.FragmentRegistrationBinding
import com.example.feature_auth.utils.navigateToActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
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

        binding.tietEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.setUsername(text.toString())
        }

        binding.tietPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.setPassword(text.toString())
        }

        binding.tietRepeatPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.setRepeatPassword(text.toString())
        }

        binding.mbtRegistration.setOnClickListener {
            viewModel.createUser()
            observeRegistrationStatus()
        }


    }

    private fun observeRegistrationStatus(){
        registrationJob = lifecycleScope.launch {
            viewModel.isRegistrationSuccesfull.collectLatest {result ->
                if(result){
                    requireContext().navigateToActivity(MAIN_ACTIVITY_PATH)
                } else{
                    Toast.makeText(requireContext(), "Registration failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStop() {
        registrationJob?.cancel()
        super.onStop()
    }

}