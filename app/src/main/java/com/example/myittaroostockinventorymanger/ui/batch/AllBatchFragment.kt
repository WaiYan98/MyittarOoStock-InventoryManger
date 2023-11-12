package com.example.myittaroostockinventorymanger.ui.batch

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.databinding.FragmentAllBatchBinding
import com.example.myittaroostockinventorymanger.databinding.FragmentBatchBinding
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration

class AllBatchFragment : Fragment(),
    BatchListRecycleViewAdapter.CallBack, MenuProvider {

    private var batchId: Long = 0
    private lateinit var selectedBatchIdList: MutableList<Long>
    lateinit var adapter: BatchListRecycleViewAdapter
    private val allBatchFragmentViewModel: AllBatchFragmentViewModel by lazy { setUpViewModel() }

    //actionMode is not null hide contextual action bar in fabBtn
    private var actionMode: ActionMode? = null

    private lateinit var binding: FragmentAllBatchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAllBatchBinding.inflate(inflater, container, false)


        setUpRecycleView()

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.fabAddBatch.setOnClickListener {
            //close contextual action bar
            actionMode?.finish()
            findNavController().navigate(BatchFragmentDirections.actionToAddAndUpdateBatchFragment())
        }

        allBatchFragmentViewModel.getAllBatchWithItem()
            ?.observe(viewLifecycleOwner) {
                //If list is empty to show empty_list_text
                if (it.isEmpty()) {
                    binding.txtEmptyBatch.visibility = View.VISIBLE
                } else {
                    binding.txtEmptyBatch.visibility = View.GONE
                }
                adapter.insertItem(it)
            }


        binding.swipeRefreshLoading.setOnRefreshListener {
            allBatchFragmentViewModel.loadStockWithBatches()
        }

        allBatchFragmentViewModel.isLoading()
            .observe(viewLifecycleOwner) {
                binding.swipeRefreshLoading.isRefreshing = it
            }

        allBatchFragmentViewModel.getMessage()
            .observe(viewLifecycleOwner) {

                val message = it?.contentIfNotHandle

                if (message != null) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }

        allBatchFragmentViewModel.getContextualActionBarTitle()
            .observe(viewLifecycleOwner) {
                actionMode?.title = it
            }

        //manage rename button hide or show
        allBatchFragmentViewModel.isShowRenameButton()
            .observe(viewLifecycleOwner) {
                actionMode?.menu?.findItem(R.id.edit)?.isVisible = it
            }
    }

    private fun setUpRecycleView() {
        adapter =
            BatchListRecycleViewAdapter()
        adapter.setCallBack(this)
        binding.recyBatchList.adapter = adapter
        binding.recyBatchList.layoutManager = LinearLayoutManager(context)
        binding.recyBatchList.addItemDecoration(VerticalSpaceItemDecoration(8))
    }

    private fun setUpViewModel() =
        ViewModelProvider(this).get(AllBatchFragmentViewModel::class.java)

    //get actionModeCallBack for Contextual action bar
    private fun getCallBack(): ActionMode.Callback {

        return object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.menuInflater?.inflate(R.menu.contextual_action_bar, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem?): Boolean {

                when (item?.itemId) {

                    R.id.delete -> {
                        findNavController()
                            .navigate(
                                BatchFragmentDirections.actionToConfirmDialogFragmentBatch(
                                    selectedBatchIdList.toLongArray()
                                )
                            )
                    }

                    R.id.edit -> {
                        mode.finish()
                        findNavController()
                            .navigate(
                                BatchFragmentDirections.actionToAddAndUpdateBatchFragment(
                                    batchId
                                )
                            )
                    }

                    R.id.select_all -> adapter.selectAllBatch()

                }

                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                adapter.contextualActionBarClose()
                Log.d("tag", "onDestroyActionMode: ")
            }
        }
    }

    override fun getHolder(holder: BatchListRecycleViewAdapter.ViewHolder) {
        holder.linearLayoutBatch.isClickable = true
    }

    //When onLongClick to start action mode
    override fun onLongClicked() {
        actionMode = activity?.startActionMode(getCallBack())!!
    }

    //to select batch list item
    override fun onItemsSelected(selectedBatchIdList: MutableList<Long>) {
        setUpContextualBarSelection(selectedBatchIdList)
    }

    override fun onSelectedItemIsOne(batchId: Long) {
        this.batchId = batchId
        Log.d("myTag", "onSelectedItemIsOne: ${this.batchId}")
    }


    /**
     *     to set contextual bar title and edit button visible or invisible
     *     and If no selected item action mod close
     */
    private fun setUpContextualBarSelection(selectedBatchIdList: MutableList<Long>) {
        this.selectedBatchIdList = selectedBatchIdList
        val num = selectedBatchIdList.size
        allBatchFragmentViewModel.setContextualActionBarTitle(num)
        allBatchFragmentViewModel.showRenameButton(num)

        if (selectedBatchIdList.isEmpty()) {
            actionMode?.finish()
        }
    }

    //create menu with menuProvider
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_app_bar_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {

            R.id.menu_search_view -> {
                val searchView = menuItem.actionView as SearchView
                searchView.setOnQueryTextListener(object : OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        val queryText = "%$newText%"
                        allBatchFragmentViewModel.searchFromDb(queryText)
                            .observe(viewLifecycleOwner) {
                                adapter.insertItem(it)
                            }
                        return false
                    }

                })
            }
        }
        return false
    }
}