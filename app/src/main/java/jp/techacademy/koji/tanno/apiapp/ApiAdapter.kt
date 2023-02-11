package jp.techacademy.koji.tanno.apiapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import jp.techacademy.koji.tanno.apiapp.databinding.RecyclerFavoriteBinding

class ApiAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    // 取得したJSONデータを解析し、Shop型オブジェクトとして生成したものを格納するリスト
    private val items = mutableListOf<Shop>()

    // 一覧画面から登録するときのコールバック（FavoriteFragmentへ通知するメソッド）
    var onClickAddFavorite: ((Shop) -> Unit)? = null   // 引数Shop型、戻りUnit型のNullable関数を定義。初期値をnullにしておく。
    // 一覧画面から削除するときのコールバック（ApiFragmentへ通知するメソッド）　　
    var onClickDeleteFavorite: ((Shop) -> Unit)? = null    // 引数Shop型、戻りUnit型のNullable関数を定義。初期値をnullにしておく。

    // Itemを押したときのメソッド
    var onClickItem: ((String) -> Unit)? = null


    fun refresh(list: List<Shop>) {
        update(list, false)
    }

    fun add(list: List<Shop>) {
        update(list, true)
    }


    // 表示リスト更新時に呼び出すメソッド
    fun update(list: List<Shop>, isAdd: Boolean) {
        items.apply {
            if(!isAdd) { // 追加のときは、clearしない
                clear() // itemsを空にする
            }
            addAll(list) // itemsにlistを全て追加する
        }
        notifyDataSetChanged() // recyclerViewを再描画させる
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // ViewHolderを継承したApiItemViewHolderオブジェクトを生成し戻す
        return ApiItemViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_favorite, parent, false))
    }

    // ViewHolderを継承したApiItemViewHolderクラスの定義
    class ApiItemViewHolder(view: View): RecyclerView.ViewHolder(view) {

        // レイアウトファイルからidがrootViewのConstraintLayoutオブジェクトを取得し、代入
        val rootView : ConstraintLayout = view.findViewById(R.id.rootView)
        // レイアウトファイルからidがnameTextViewのTextViewオブジェクトを取得し、代入
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        // レイアウトファイルからidがaddressTextViewのTextViewオブジェクトを取得し、代入
        val addressTextView: TextView = view.findViewById(R.id.addressTextView)
        // レイアウトファイルからidがimageViewのImageViewオブジェクトを取得し、代入
        val imageView: ImageView = view.findViewById(R.id.imageView)
        // レイアウトファイルからidがfavoriteImageViewのImageViewオブジェクトを取得し、代入
        val favoriteImageView: ImageView = view.findViewById(R.id.favoriteImageView)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ApiItemViewHolder) {
            // 生成されたViewHolderがApiItemViewHolderだったら。。。
            updateApiItemViewHolder(holder, position)
        }// {
            // 別のViewHolderをバインドさせることが可能となる
        // }
    }

    private fun updateApiItemViewHolder(holder: ApiItemViewHolder, position: Int) {
        // 生成されたViewHolderの位置を指定し、オブジェクトを代入
        val data = items[position]
        // お気に入り状態を取得
        val isFavorite = FavoriteShop.findBy(data.id) != null
        holder.apply {
            rootView.apply {
                // 偶数番目と奇数番目で背景色を変更させる
                setBackgroundColor(
                    ContextCompat.getColor(context,
                        if (position % 2 == 0) android.R.color.white else android.R.color.darker_gray))
                setOnClickListener {
                    onClickItem?.invoke(if (data.couponUrls.sp.isNotEmpty()) data.couponUrls.sp else data.couponUrls.pc)
                }
            }
            // nameTextViewのtextプロパティに代入されたオブジェクトのnameプロパティを代入
            nameTextView.text = data.name
            addressTextView.text = data.address
            Log.v("DEBUG",data.name + " : " + data.address)
            // Picassoライブラリを使い、imageViewにdata.LogoImageのurlの画像を読み込ませる
            Picasso.get().load(data.logoImage).into(imageView)
            // 白抜きの星マークの画像を指定
            favoriteImageView.apply {
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
    }

    override fun getItemCount(): Int {
        return items.size
    }
}