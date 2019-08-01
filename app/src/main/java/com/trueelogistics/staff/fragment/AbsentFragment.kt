package com.trueelogistics.staff.fragment


import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.trueelogistics.staff.activity.MainActivity
import com.trueelogistics.staff.R
import kotlinx.android.synthetic.main.fragment_absent.*
import java.util.*

class AbsentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_absent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity
        toolbar.setOnClickListener {
            mainActivity.actionToolbar()
        }
        radio_group.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->

            })
        dateEnd.isEnabled = false
        val c = Calendar.getInstance()
        var dayStart = c.get(Calendar.DAY_OF_MONTH)
        var monthStart = c.get(Calendar.MONTH)
        var yearStart = c.get(Calendar.YEAR)
        dateStart.setOnClickListener {
            dayStart = c.get(Calendar.DAY_OF_MONTH)
            monthStart = c.get(Calendar.MONTH)
            yearStart = c.get(Calendar.YEAR)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH)
            val year = c.get(Calendar.YEAR)
            activity?.let {
                val dpd = DatePickerDialog(it, DatePickerDialog.OnDateSetListener
                { view, pickYear, monthOfYear, dayOfMonth ->
                    dayStart = dayOfMonth
                    monthStart = monthOfYear
                    yearStart = pickYear
                    dateStartShow.text = String.format(this.getString(R.string.show_date_pick)
                        , dayOfMonth, monthOfYear, yearStart) }, year, month, day )
                dpd.show()
            }
            dateEndShow.text = this.getString(R.string.date_absence_end_text)
            dateEnd.isEnabled = true
        }
        dateEnd.setOnClickListener {
            activity?.let {
                val dpd = DatePickerDialog(it, DatePickerDialog.OnDateSetListener
                { view, pickYear, monthOfYear, dayOfMonth ->
                    dateEndShow.text = String.format(this.getString(R.string.show_date_pick)
                        , dayOfMonth, monthOfYear, pickYear) }, yearStart, monthStart, dayStart)
                val dateFromStart = c.set(yearStart,monthStart,dayStart)
                dpd.datePicker.minDate = c.timeInMillis
                dpd.show()
            }
        }
    }

}
