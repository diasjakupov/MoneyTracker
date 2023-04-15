package com.example.myapplication.ui.elements

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.example.myapplication.R
import com.example.myapplication.data.models.RemoteCheque
import com.example.myapplication.data.network.NetResponse
import com.example.myapplication.databinding.FragmentQRScannerBinding
import com.example.myapplication.ui.viewmodels.TransactionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch



class QRScanner : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private var _binding: FragmentQRScannerBinding? = null
    private val binding: FragmentQRScannerBinding = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentQRScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            Log.e("TAG", "Enter callback")
            if(it.text.contains("consumer.oofd.kz")){
                binding.parsedUrl.text = it.text
            }else{
                binding.parsedUrl.text = "consumer.oofd.kz/ticket/b411039b-286d-4ebe-b81b-ee9d233ec573"
            }

        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            QRScanner()
    }
}


@Composable
fun QRScannerComp(navController: NavController, viewModel: TransactionsViewModel){
    val isLoadingDialogShown = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true, block = {
        viewModel.getTransactionInfoByQr("consumer.oofd.kz/ticket/b411039b-286d-4ebe-b81b-ee9d233ec573")
        viewModel.fetchedTransactions.collectLatest {
            if(it != null){
                when(it){
                    is NetResponse.Successful<RemoteCheque?>->{
                        Log.e("TAG", "HUH?")
                        isLoadingDialogShown.value = false
                        navController.navigateUp()
                    }
                    is NetResponse.Loading<RemoteCheque?>->{
                        isLoadingDialogShown.value = true
                    }
                    else->{}
                }
            }
        }
    })
    if(isLoadingDialogShown.value){
        LoadingDialogFull()
    }
//    AndroidViewBinding(factory = (FragmentQRScannerBinding::inflate)){
//        if(!this.parsedUrl.text.isNullOrBlank()){
//            // TODO(test on real device)
//            viewModel.getTransactionInfoByQr(this.parsedUrl.text.toString())
//        }
//    }
//    if(networkState.value != null){
//        when(networkState.value){
//            is NetResponse.Successful<RemoteCheque?>->{
//                navController.navigateUp()
//            }
//            is NetResponse.Loading<RemoteCheque?>->{
////                LoadingDialogFull()
//            }
//            else->{}
//        }
//    }

}