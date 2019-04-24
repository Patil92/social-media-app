
package com.companies.patil.twitter_demo_final

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Build.ID
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.*
import android.widget.*
import com.companies.patil.twitter_demo_final.R.id.chats
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Comment
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.security.AccessControlContext
import java.text.SimpleDateFormat
import java.util.*

class ChatSMS : AppCompatActivity() {

    var ListChat = ArrayList<chatUser>()
    private var adpater: MycustomChatAdapter? = null

    private var chats: String? = null
    private var mAuth: FirebaseAuth? = null
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference

    var chatemail: String? = null
    var UserUID: String? = null
    var ID: String? = null
    var to: String? = null
    var myemail: String? = null
    /* val connectEmail=findViewById<EditText>(R.id.connectEmail)
    val ChatConnect=findViewById<ImageView>(R.id.Connect_btn)
    val chat=findViewById<EditText>(R.id.chat)
    val send=findViewById<ImageView>(R.id.send)*/

    var chatID: String? = null
    var wordListContents:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_sms)

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        //val listview=findViewById<ListView>(R.id.chat_view)


        //ListChat.add(chatUser("Patil92","Welocme to Patil's World..."))
        // listview.adapter= MycustomChatAdapter(this,ListChat)


        val ChatConnect = findViewById<ImageView>(R.id.Connect_btn)
        val chat = findViewById<EditText>(R.id.chat)
        val send = findViewById<ImageView>(R.id.send)
        val connectEmail=findViewById<EditText>(R.id.connectEmail)
        // ListChat.add(chatUser("Welcome TO Patil's World...!!! ",myemail.toString()))

        //adpater=ChatSMS(this,ListChat)

        ChatConnect.setOnClickListener(View.OnClickListener {

             to=connectEmail.text.toString()

           findViewById<TextView>(R.id.chatter_name).setText(to)

            // getId()

            /*var toast: Toast = Toast.makeText(applicationContext,  to, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
            toast.show()*/

            LoadChats()

        })

        send.setOnClickListener(View.OnClickListener {
            val chatsms = chat.text.toString()
            chatemail = findViewById<EditText>(R.id.connectEmail).text.toString()

            //ListChat.add(chatUser("Patil",chatsms.toString()))
            // adpater!!.notifyDataSetChanged()

            checkPSellJson(chatsms)
            /*if(checkPSellJson(chatsms))
            {
                var toast: Toast = Toast.makeText(applicationContext, "Correct Word..", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                toast.show()
            }
            else
            {
                var toast: Toast = Toast.makeText(applicationContext, "Wrong Word..", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                toast.show()
            }*/
            //sendpost(chatsms.toString(), to!!)
            //sendpost(chatsms.toString())

        })

        //LoadChats()

    }

    fun checkPSellJson(sms:String)
    {
        val url="https://agri-django-server-public-patil.c9users.io/Agri/?sms="+sms
        AsyncTaskHandleJson().execute(url)
    }

    inner class AsyncTaskHandleJson:AsyncTask<String,String,String>(){
        override fun doInBackground(vararg url: String?): String {
            var text:String

            val connection=URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text=connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            }finally {
                connection.disconnect()
            }
            return text
        }

        override  fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson(result)
        }
    }

    private fun handleJson(jsonString: String?)
    {

       // val res=jsonString!!
        val jsonObject=JSONObject(jsonString)
        val res=jsonObject.getString("res").toString()

        if(res.equals("true"))
        {
            val chatsms = findViewById<EditText>(R.id.chat).text.toString()
            sendpost(chatsms)
            findViewById<EditText>(R.id.chat).setText("")
        }
        else{
            var toast: Toast = Toast.makeText(applicationContext, "Invalid String...", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
            toast.show()
        }



    }

    fun checkSPellReadFile(sms:String)
    {
        /*val charset=Charsets.UTF_8

        val files = File("C:\\Users\\Abhishek S Patil\\Programs\\Android\\Twitter_demo_final\\app\\src\\main\\res\\words.txt")
        val readBytes = Files.readAllBytes(files.toPath())
        wordListContents = String(readBytes,charset )
        return wordListContents!!.contains(sms)

        val file = File("C:\\\\Users\\\\Abhishek S Patil\\\\Programs\\\\Android\\\\Twitter_demo_final\\\\app\\\\src\\\\main\\\\res\\\\words.txt")
        var lines = file.readLines()

            if(lines.contains(sms))
                return true

        return false*/
       // val url="https://agri-django-server-public-patil.c9users.io/Agri/?sms="+sms
      //  val apiResponse = URL(sms).readText()

    }

    fun checkSpell(sms:String) : Boolean
    {
        return wordListContents!!.contains(sms)
    }

    override fun onStart() {
        super.onStart()

        var b: Bundle = intent.extras
        ID = b.getString("UserUID")
        myemail = b.getString("email")
        // getId()
        myemail=SplitString(myemail!!)

       /* var toast: Toast = Toast.makeText(applicationContext, myemail, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
        toast.show()*/
    }

    fun SplitString(email:String):String{
        val split= email.split("@")
        return split[0]
    }

    fun getId() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                chatID = dataSnapshot.child("User_Id").value.toString()

                var td = dataSnapshot!!.value as HashMap<String, Any>

                for (key in td.keys) {

                    var post = td[key] as HashMap<String, Any>

                    if (post["email"] == myemail)
                        chatID = post["User_Id"] as String

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        myRef.child("Users").addValueEventListener(postListener)
    }




    fun sendpost(sms: String) {

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        //var currentUser =mAuth!!.currentUser

        //val currentUid=currentUser!!.uid
        //myRef.child("Users").child(ID).child("User_Id")

        chatemail = findViewById<EditText>(R.id.connectEmail).text.toString()
        //  myRef.child("Chats").child(myemail).child(to).push().setValue(chatUser(currentDate, sms))
        // myRef.child("Chats").child(to).child(myemail).push().setValue(chatUser(currentDate, sms))

        myRef.child("Chats").child("Patil").child("Patil92").push().setValue(chatUser(currentDate, sms))
        val chat = findViewById<EditText>(R.id.chat)
        chat.setText("")

    }

    fun LoadChats() {

        myRef.child("Chats").child("Patil").child("Patil92")
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        chats=" "
                        chats="\n\n"
                        try {

                            //ListChat.add(chatUser("Patil92","Welcome to Patil's World...!!!"))

                            var td = dataSnapshot!!.value as HashMap<String, Any>

                            for (key in td.keys) {

                                var post = td[key] as HashMap<String, Any>

                                // ListChat.add(chatUser(post["userID"] as String,post["sms"] as String))
                                //adpater!!.notifyDataSetChanged()

                                var userid=post["date"] as String
                                var sms=post["sms"] as String

                                //chats+="User : "+userid+"\n"+sms+"\n\n"
                                chats += sms
                                chats+="\n\n"

                                findViewById<TextView>(R.id.chatShow).setText(chats)
                            }

                            //findViewById<TextView>(R.id.chatShow).setText(chats)


                            //adpater!!.notifyDataSetChanged()

                        } catch (ex: Exception) {

                            Toast.makeText(applicationContext,"Error in Loading ...asdgf"+"\n"+ex, Toast.LENGTH_LONG).show()
                        }

                        // dataSnapshot!!.children.mapNotNullTo(ListChat) { it.getValue<chatUser>(chatUser::class.java) }

                        //adpater!!.notifyDataSetChanged()
                    }

                    override fun onCancelled(p0: DatabaseError?) {
                        Toast.makeText(applicationContext,"DataBase Error ...", Toast.LENGTH_LONG).show()
                    }
                })

        /* to = findViewById<TextView>(R.id.chatter_name).text.toString()
         chatemail = findViewById<EditText>(R.id.connectEmail).text.toString()

         chatemail=SplitString(chatemail!!)
         findViewById<EditText>(R.id.connectEmail).setText("")
         findViewById<TextView>(R.id.chatter_name).setText(chatemail)

         var toast: Toast = Toast.makeText(applicationContext, "Connecting... " + chatemail, Toast.LENGTH_SHORT)
         toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
         toast.show()

         myRef.child("Chats").child(myemail).child(to)
                 .addValueEventListener(object : ValueEventListener {

                     override fun onDataChange(dataSnapshot: DataSnapshot?) {

                         try {

                             chats = "\n"

                             //ListChat.add(chatUser("Patil92","Welcome to Patil's World...!!!"))

                             var td = dataSnapshot!!.value as HashMap<String, Any>

                             for (key in td.keys) {

                                 var post = td[key] as HashMap<String, Any>

                                 // ListChat.add(chatUser(post["userID"] as String,post["sms"] as String))
                                 //adpater!!.notifyDataSetChanged()

                                 var time = post["date"] as String
                                 var sms = post["sms"] as String

                                 //chats+="Sent Time : "+time+"\n"+sms+"\n\n"
                                 chats += sms + "\n\n"
                                 findViewById<TextView>(R.id.chatShow).setText(chats)
                             }
                             //adpater!!.notifyDataSetChanged()

                         } catch (ex: Exception) {

                             Toast.makeText(applicationContext, "Error in Loading ..." + "\n" + ex, Toast.LENGTH_LONG).show()
                         }

                         // dataSnapshot!!.children.mapNotNullTo(ListChat) { it.getValue<chatUser>(chatUser::class.java) }
                         //adpater!!.notifyDataSetChanged()
                     }

                     override fun onCancelled(p0: DatabaseError?) {
                         Toast.makeText(applicationContext, "DataBase Error ...", Toast.LENGTH_LONG).show()
                     }
                 })

         toast = Toast.makeText(applicationContext, "Connected ", Toast.LENGTH_SHORT)
         toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
         toast.show()*/

    }

    fun Load() {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                ListChat.clear()
                val a: chatUser = dataSnapshot.getValue(chatUser::class.java)!!
                ListChat.add(a)
                adpater!!.notifyDataSetChanged()

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {

                val newComment = dataSnapshot.getValue(Comment::class.java)
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val movedComment = dataSnapshot.getValue(Comment::class.java)
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {

                Toast.makeText(applicationContext, "Error in Loading ..." + "\n" + databaseError, Toast.LENGTH_LONG).show()
            }
        }

        myRef.child("Chats").addChildEventListener(childEventListener)

    }
}

private class MycustomChatAdapter(context: Context, listNotesAdpater: ArrayList<chatUser>) : BaseAdapter() {
    private val mContext: Context
    var mychatList = ArrayList<chatUser>()

    init {
        mContext = context
        mychatList = listNotesAdpater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var mytweet = mychatList[position]

        val layoutInflater = LayoutInflater.from(mContext)
        val rowMain = layoutInflater.inflate(R.layout.chat_views, null)

        val senderemail = rowMain.findViewById<TextView>(R.id.Sender_email)
        val chatshow = rowMain.findViewById<TextView>(R.id.chats)

        val mychat = mychatList[position]

        senderemail.setText(mychat.userID)
        chatshow.setText(mychat.sms)

        return rowMain
    }

    override fun getItem(position: Int): Any {
        return mychatList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mychatList.size
    }
}



/////////


