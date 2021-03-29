package com.albertmartorell.qustodio

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.albertmartorell.qustodio.databinding.ActivityMainBinding
import com.albertmartorell.qustodio.utils.getCurrentTimeInMilliseconds
import com.albertmartorell.qustodio.utils.getJsonFromAsset
import com.albertmartorell.qustodio.utils.setEvent
import com.albertmartorell.qustodio.utils.sortedByTimeStamp
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var list = mutableListOf<Events>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialData()
        listener()

    }

    private fun initialData() {

        list = sortedByTimeStamp(getJsonFromAsset(applicationContext, "test.json"))

        binding.textTime.text = getCurrentTimeInMilliseconds().toString()
        ArrayAdapter.createFromResource(
            this,
            R.array.type_arrays,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.editTextType.adapter = adapter
        }


    }

    private fun listener() {

        binding.button.setOnClickListener {

            val initialSize = list.size
            list = setEvent(
                list.toMutableList(),
                binding.textTime.text.toString().toLong(),
                binding.editTextUserId.text.toString().toLong(),
                binding.editTextType.selectedItem.toString()
            ) as MutableList<Events>

            if (initialSize != list.size) {

                val snackbar = Snackbar
                    .make(binding.root, "New event added!", Snackbar.LENGTH_LONG)
                snackbar.show()
                binding.editTextUserId.setText("")

            } else {

                val snackbar = Snackbar
                    .make(
                        binding.root,
                        "The userId already exists. Try it again",
                        Snackbar.LENGTH_LONG
                    )
                snackbar.show()

            }

            binding.textTime.text = getCurrentTimeInMilliseconds().toString()
            binding.editTextType.setSelection(0)

        }

    }

}
