package com.brandon.news_mvvm.db

import android.content.Context
import androidx.room.*
import com.brandon.news_mvvm.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase(){

    abstract fun getArticleDao(): ArticleDao

    companion object {
        /*
        * @Volatile 키워드는 변수를 "가시성이 보장된 변수"로 표시합니다.
        * 이는 해당 변수가 변경되면 다른 스레드에서 이를 즉시 볼 수 있음을 보장합니다.
        * 다시 말해, 하나의 스레드에서 변수를 변경하면,
        * 이 변경 사항은 다른 스레드에서 즉시 볼 수 있으므로 데이터 일관성과 정확성을 유지하고 동기화 문제를 방지하는 데 도움이 됩니다.
        * */
        @Volatile
        private var instance: ArticleDatabase? = null
        private val LOCK = Any()

        // invoke is called everytime database is created
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            // code here cannot be accessed from the multi sources at the same time
            instance ?: createDatabase(context).also{ instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }
}