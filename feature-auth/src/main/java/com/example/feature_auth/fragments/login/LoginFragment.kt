package com.example.feature_auth.fragments.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.core.util.Constants.Companion.MAIN_ACTIVITY_PATH
import com.example.feature_auth.BR
import com.example.feature_auth.LoginActivity
import com.example.feature_auth.R
import com.example.feature_auth.databinding.FragmentLoginBinding
import com.example.feature_auth.utils.navigateToActivity
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    private val googleLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = viewModel.getSignedInAccountFromIntent(result)
            runCatching {
                val account = task.getResult(ApiException::class.java)
                Log.d("DUPA", "BEFORE signInGoogle account ${account.email}")
                viewModel.signInGoogle(account.idToken, onSuccess = {
                    requireContext().navigateToActivity(MAIN_ACTIVITY_PATH)
                })
//                requireContext().navigateToActivity(MAIN_ACTIVITY_PATH)

            }.onFailure {
                Log.e(tag, it.stackTraceToString())
                Log.d("DUPA", "googleLoginResult ${it.stackTraceToString()}")
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.setVariable(BR.viewModel, viewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mbtLogin.setOnClickListener {
            if(viewModel.checkLoginData()){
                viewModel.singIn()
                observeLoginStatus()
            } else {
                Toast.makeText(requireContext(), "Incorrect e-mail or password.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        binding.mbtLoginGoogle.setOnClickListener {
            onGoogleLoginClicked()
        }
        binding.mbtRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragmentNav_to_registrationFragmentNav)
        }
    }


    private fun observeLoginStatus(){
        lifecycleScope.launch {
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

    fun onGoogleLoginClicked() {
        viewModel.initGoogleManager(activity as LoginActivity)
        googleLoginResult.launch(viewModel.getGoogleClient())
    }
}