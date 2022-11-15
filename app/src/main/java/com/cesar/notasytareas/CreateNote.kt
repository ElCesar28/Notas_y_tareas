package com.cesar.notasytareas

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.cesar.notasytareas.data.NoteDatabase
import com.cesar.notasytareas.databinding.FragmentCreateNoteBinding
import com.cesar.notasytareas.model.Note
import kotlinx.coroutines.launch

class CreateNote : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCreateNoteBinding>(inflater,
            R.layout.fragment_create_note,container,false)

        //edit
        var id = -1;
        if(arguments?.getString("title") != null) {
            binding.title.setText(arguments?.getString("title"))
            binding.description.setText(arguments?.getString("description"))
            id = arguments?.getString("id")!!.toInt()
        }

        binding.save.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("title", binding.title.text.toString())
            bundle.putString("description", binding.description.text.toString())
            parentFragmentManager.setFragmentResult("key",bundle)
            //Insert
            lifecycleScope.launch{
                if (id == -1){
                    //insert new note
                    val newNote = Note( binding.title.text.toString(),binding.description.text.toString(),2,"","",false)
                    NoteDatabase.getDatabase(requireActivity().applicationContext).noteDao().insert(newNote)
                    NoteDatabase.getDatabase(requireActivity().applicationContext).noteDao().getAllNotes()
                    it.findNavController().navigate(R.id.action_createNote_to_listNote)
                } else {
                    //update note
                    val builder = AlertDialog.Builder(activity)
                    builder.apply{
                        setTitle(R.string.editDialogAlertTitle)
                        setMessage(R.string.editDialogAlertMessage)
                        setPositiveButton(getString(R.string.ok), DialogInterface.OnClickListener { dialog, which ->
                            NoteDatabase.getDatabase(requireActivity().applicationContext).noteDao().updateNote(binding.title.text.toString(),binding.description.text.toString(),id)
                            NoteDatabase.getDatabase(requireActivity().applicationContext).noteDao().getAllNotes()
                            it.findNavController().navigate(R.id.action_createNote_to_listNote)
                            dialog.cancel()
                        })
                        setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener { dialog, _ ->
                            dialog.cancel()
                        })
                    }
                    val adapter = builder.create()
                    adapter.show()
                }

            }

            //Navigation

        }

        binding.btnCancel.setOnClickListener {
            //Navigation
            it.findNavController().navigate(R.id.action_createNote_to_listNote)
        }

        return binding.root
    }

}