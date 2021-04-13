package com.fozimat.consumerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fozimat.consumerapp.adapter.UserAdapter
import com.fozimat.consumerapp.databinding.FragmentFollowBinding
import com.fozimat.consumerapp.viewModel.FollowingViewModel

class FollowingFragment : Fragment() {

    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username : String?) : FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding : FragmentFollowBinding? = null
    private lateinit var adapter : UserAdapter
    private val binding get() = _binding!!
    private lateinit var followingViewModel : FollowingViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showRecyclerView()
        showLoading(true)
        setFollowing()
    }

    private fun setFollowing() {
        if(arguments != null) {
            val username = arguments?.getString(ARG_USERNAME)
            followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)

            if(username != null) {
                followingViewModel.setFollowing(username)
            }

            followingViewModel.getFollowing().observe(viewLifecycleOwner, {usersItems ->
                if(usersItems != null) {
                    adapter.setData(usersItems)
                    showLoading(false)
                }
            })
        }
    }

    private fun showRecyclerView() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.rvFollow.layoutManager = LinearLayoutManager(activity)
        binding.rvFollow.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}