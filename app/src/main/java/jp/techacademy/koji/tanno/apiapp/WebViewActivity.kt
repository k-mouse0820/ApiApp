package jp.techacademy.koji.tanno.apiapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    // お気に入り状態を取得
    val isFavorite =  null
/*
    var onClickAddFavorite: ((FavoriteShop) -> Unit)? = null
    var onClickDeleteFavorite: ((FavoriteShop) -> Unit)? = null

    favoriteImageView.setOnClickListener {
        onClickDeleteFavorite?.invoke(data)
        notifyItemChanged(position)
    }
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView.loadUrl(intent.getStringExtra(KEY_URL).toString())



        webFavoriteImageView.apply {
            setImageResource(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_border) // Picassoというライブラリを使ってImageViewに画像をはめ込む
            setOnClickListener{
                if (isFavorite) {
                    onClickDeleteFavorite?.invoke(data)
                } else {
                    onClickAddFavorite?.invoke(data)
                }
                notifyItemChanged(position)
            }
        }



    }





    companion object {
        private const val KEY_URL = "key_url"
        private const val KEY_ID = "key_id"
        fun start(activity: Activity, url: String, id: String) {
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            intent.putExtra(KEY_ID, id)
            activity.startActivity(intent)

        }
    }

}