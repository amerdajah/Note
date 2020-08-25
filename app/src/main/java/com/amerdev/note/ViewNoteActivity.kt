package com.amerdev.note

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import com.amerdev.note.models.Note
import com.amerdev.note.persistence.NoteRepository
import com.amerdev.note.util.Utility
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_view_note.*
import kotlinx.android.synthetic.main.layout_view_note_toolbar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ViewNoteActivity : AppCompatActivity(), View.OnTouchListener, GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener, View.OnClickListener, TextWatcher{

    private val EDIT_MODE_ENABLED = 1
    private val EDIT_MODE_DISABLED = 0

    private var isNewNote = false
    lateinit var gestureDetector: GestureDetector
    private lateinit var mInitialNote: Note
    private lateinit var mFinalNote: Note
    private lateinit var mNoteRepository: NoteRepository
    private var mMode = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_note)

        mNoteRepository = NoteRepository(this)

        when (getIncomingIntent()){
            true -> {
                //edit mode
                setNewNote()
                enableEditMode()
            }
            false -> {
                //view mode
                setNote()
                disableContentInteraction()
            }
        }
        setListeners()
    }

    override fun onClick(p0: View?) {
        when (p0?.id){
            R.id.toolbar_check -> {
                disableEditMode()
            }
            R.id.toolbar_textView -> {
                enableEditMode()
                toolbar_editText.requestFocus()
                toolbar_editText.setSelection(toolbar_editText.length())
            }
            R.id.toolbar_back_arrow -> {
                finish()
            }
        }
    }

    private fun saveNewNote(){
        CoroutineScope(IO).launch {
            mNoteRepository.insertNoteTask(mFinalNote)
        }
    }

    private fun saveChanges(){
        if (isNewNote){
            saveNewNote()
        }else {
            updateNote()
        }
    }

    private fun updateNote(){
        CoroutineScope(IO).launch {
            mNoteRepository.updateNote(mFinalNote)
        }
    }

    private fun enableEditMode(){
        back_arrow_container.visibility = View.GONE
        check_container.visibility = View.VISIBLE

        toolbar_textView.visibility = View.GONE
        toolbar_editText.visibility = View.VISIBLE

        mMode = EDIT_MODE_ENABLED

        enableContentInteraction()
    }

    private fun disableEditMode() {
        hideSoftKeyboard()
        back_arrow_container.visibility = View.VISIBLE
        check_container.visibility = View.GONE

        toolbar_textView.visibility = View.VISIBLE
        toolbar_editText.visibility = View.GONE

        mMode = EDIT_MODE_DISABLED

        disableContentInteraction()
        var temp = lined_editText.text.toString()
        temp = temp.replace("\n", "")
        temp = temp.replace(" ", "")
        if (temp.isNotEmpty()){
            mFinalNote.title = toolbar_editText.text.toString()
            mFinalNote.content = lined_editText.text.toString()
            mFinalNote.timeStamp = Utility.currentTimeStamp!!
            if (mFinalNote.title != mInitialNote.title || mFinalNote.content != mInitialNote.content){
                saveChanges()
            }
        }
    }

    private fun hideSoftKeyboard(){
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    private fun setListeners(){
        lined_editText.setOnTouchListener(this)
        gestureDetector = GestureDetector(this, this)
        toolbar_textView.setOnClickListener(this)
        toolbar_check.setOnClickListener(this)
        toolbar_back_arrow.setOnClickListener(this)
        toolbar_editText.addTextChangedListener(this)
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(p1)
    }

    private fun getIncomingIntent(): Boolean {
        if (intent.hasExtra("note")){
            mMode = EDIT_MODE_DISABLED
            mInitialNote = intent.getParcelableExtra<Note>("note")!!
            mFinalNote = Note("","","")
            mFinalNote.id = mInitialNote.id
            mFinalNote.title = mInitialNote.title
            mFinalNote.content = mInitialNote.content
            mFinalNote.timeStamp = mInitialNote.timeStamp
            return false
        }
        mMode = EDIT_MODE_ENABLED
        isNewNote = true
        return true
    }

    private fun setNote(){
        toolbar_editText.setText(mInitialNote.title)
        toolbar_textView.text = mInitialNote.title
        lined_editText.setText(mInitialNote.content)
    }

    private fun setNewNote(){
        mInitialNote = Note("Note Title", "", "")
        mFinalNote = Note("Note Title", "", "")

        toolbar_textView.text = mInitialNote.title
        toolbar_editText.setText(mInitialNote.title)
    }

    private fun disableContentInteraction(){
        lined_editText.keyListener = null
        lined_editText.isFocusable = false
        lined_editText.isFocusableInTouchMode = false
        lined_editText.isCursorVisible = false
        lined_editText.clearFocus()
    }

    private fun enableContentInteraction(){
        lined_editText.keyListener = TextInputEditText(this).keyListener
        lined_editText.isFocusable = true
        lined_editText.isFocusableInTouchMode = true
        lined_editText.isCursorVisible = true
        lined_editText.requestFocus()
    }

    override fun onDoubleTap(p0: MotionEvent?): Boolean {
        enableEditMode()
        return false
    }

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent?) {

    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {

    }

    override fun onBackPressed() {
        if (mMode == EDIT_MODE_ENABLED){
            disableEditMode()
        }else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("mode", mMode)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val mode = savedInstanceState.getInt("mode")
        if (mode == EDIT_MODE_ENABLED){
            enableEditMode()
        }
    }

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        toolbar_textView.text = p0
    }
}