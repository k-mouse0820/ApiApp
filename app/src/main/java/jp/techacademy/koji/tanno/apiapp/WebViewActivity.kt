package jp.techacademy.koji.tanno.apiapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.techacademy.koji.tanno.apiapp.FavoriteShop.Companion.findBy
import kotlinx.android.synthetic.main.activity_web_view.*
import java.io.Serializable

class WebViewActivity : AppCompatActivity() {

    inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        //Web読み込み
        webView.loadUrl(intent.getStringExtra(KEY_URL).toString())

        // Shop情報を受取
        val id = intent.getStringExtra(KEY_ID).toString()
        val name = intent.getStringExtra(KEY_NAME).toString()
        val address = intent.getStringExtra(KEY_ADDRESS).toString()
        val imageUrl = intent.getStringExtra(KEY_IMAGEURL).toString()

        // お気に入り状態を取得
        val isFavorite = FavoriteShop.findBy(id) != null

        // お気に入りマークを表示し、リスナーセット
        webFavoriteImageView.apply {
            setImageResource(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_border) // Picassoというライブラリを使ってImageViewに画像をはめ込む
            setOnClickListener{
                if (isFavorite) {
                    MainActivity().onDeleteFavorite(id)
                    setImageResource(R.drawable.ic_star_border)
                } else {
                    FavoriteShop.insert(FavoriteShop().apply {
                        this.id = id
                        this.name = name
                        this.address = address
                        this.imageUrl = imageUrl
                        this.url = url
                    })
                    setImageResource(R.drawable.ic_star)
                    //MainActivity().updateFragments()
                }
            }
        }
    }





    companion object {
        private const val KEY_URL = "key_url"
        private const val KEY_ID = "key_id"
        private const val KEY_NAME = "key_name"
        private const val KEY_ADDRESS = "key_address"
        private const val KEY_IMAGEURL = "key_imageUrl"
        fun start(activity: Activity, url: String, id: String, name: String, address: String, imageUrl: String) {
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            intent.putExtra(KEY_ID, id)
            intent.putExtra(KEY_NAME, name)
            intent.putExtra(KEY_ADDRESS, address)
            intent.putExtra(KEY_IMAGEURL, imageUrl)
            activity.startActivity(intent)

        }
    }

}
