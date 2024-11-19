package vn.edu.hust.studentman

import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
  private val students = mutableListOf(
    StudentModel("Nguyễn Văn An", "SV001"),
    StudentModel("Trần Thị Bảo", "SV002"),
    StudentModel("Lê Hoàng Cường", "SV003"),
    StudentModel("Phạm Thị Dung", "SV004"),
    StudentModel("Đỗ Minh Đức", "SV005"),
    StudentModel("Vũ Thị Hoa", "SV006"),
    StudentModel("Hoàng Văn Hải", "SV007"),
    StudentModel("Bùi Thị Hạnh", "SV008"),
    StudentModel("Đinh Văn Hùng", "SV009"),
    StudentModel("Nguyễn Thị Linh", "SV010"),
    StudentModel("Phạm Văn Long", "SV011"),
    StudentModel("Trần Thị Mai", "SV012"),
    StudentModel("Lê Thị Ngọc", "SV013"),
    StudentModel("Vũ Văn Nam", "SV014"),
    StudentModel("Hoàng Thị Phương", "SV015"),
    StudentModel("Đỗ Văn Quân", "SV016"),
    StudentModel("Nguyễn Thị Thu", "SV017"),
    StudentModel("Trần Văn Tài", "SV018"),
    StudentModel("Phạm Thị Tuyết", "SV019"),
    StudentModel("Lê Văn Vũ", "SV020")
  )

  private lateinit var studentAdapter: StudentAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    studentAdapter = StudentAdapter(students, ::editStudent, ::removeStudent)
    findViewById<RecyclerView>(R.id.recycler_view_students).apply {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    // Add new student
    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      val dialogView = LayoutInflater.from(this@MainActivity)
        .inflate(R.layout.layout_alert_dialog, null)

      val editHoten = dialogView.findViewById<EditText>(R.id.edit_hoten)
      val editMssv = dialogView.findViewById<EditText>(R.id.edit_mssv)

      AlertDialog.Builder(this)
        .setTitle("Nhập thông tin sinh viên")
        .setView(dialogView)
        .setPositiveButton("Thêm") { _, _ ->
          val hoten = editHoten.text.toString()
          val mssv = editMssv.text.toString()
          if (hoten.isNotEmpty() && mssv.isNotEmpty()) {
            addStudent(StudentModel(hoten, mssv))
            Log.v("TAG", "$hoten: $mssv")
          }
        }
        .setNegativeButton("Thoát", null)
        .create()
        .show()
    }
  }

  //Edit
  private fun editStudent(position: Int) {
    val currentStudent = students[position]

    val dialog = Dialog(this@MainActivity)
    dialog.setContentView(R.layout.layout_dialog)

    val editHoten = dialog.findViewById<EditText>(R.id.edit_hoten)
    val editMssv = dialog.findViewById<EditText>(R.id.edit_mssv)
    editHoten.setText(currentStudent.studentName)
    editMssv.setText(currentStudent.studentId)

    dialog.findViewById<Button>(R.id.button_ok).setOnClickListener {
      val hoten = editHoten.text.toString()
      val mssv = editMssv.text.toString()

      if (hoten.isNotEmpty() && mssv.isNotEmpty()) {
        students[position] = StudentModel(hoten, mssv)
        studentAdapter.notifyItemChanged(position)
      }
      Log.v("TAG", "$hoten: $mssv")
      dialog.dismiss()
    }

    dialog.findViewById<Button>(R.id.button_cancel).setOnClickListener {
      dialog.dismiss()
    }

    dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    dialog.show()
  }

  //Remove
  private fun removeStudent(position: Int) {
    val currentStudent = students[position]

    AlertDialog.Builder(this)
      .setTitle("Bạn có thực sự muốn xóa sinh viên ${currentStudent.studentName}?")
      .setPositiveButton("Có") { _, _ ->
        students.removeAt(position)
        studentAdapter.notifyItemRemoved(position)

        val view = findViewById<View>(R.id.recycler_view_students)
        Snackbar.make(view, "Đã xóa sinh viên ${currentStudent.studentName}", Snackbar.LENGTH_LONG)
          .setAction("Undo") {
               students.add(position, currentStudent)
            studentAdapter.notifyItemInserted(position)
          }
          .show()
      }
      .setNegativeButton("Không", null) 
      .create()
      .show()
  }

  private fun addStudent(student: StudentModel) {
    students.add(student)
    studentAdapter.notifyItemInserted(students.size - 1)
  }
}