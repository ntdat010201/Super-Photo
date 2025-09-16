package com.example.superphoto.domain.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.superphoto.R
import com.example.superphoto.databinding.FragmentMineBinding
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.core.scope.Scope
import kotlin.getValue

class MineFragment : Fragment(), AndroidScopeComponent {
    override val scope: Scope by fragmentScope()

    private lateinit var binding: FragmentMineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMineBinding.inflate(layoutInflater)

        return binding.root
    }

}