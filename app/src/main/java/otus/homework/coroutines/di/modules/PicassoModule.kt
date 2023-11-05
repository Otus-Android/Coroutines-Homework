package otus.homework.coroutines.di.modules

import android.content.Context
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import otus.homework.coroutines.di.annotations.qualifier.ApplicationContext
import otus.homework.coroutines.di.annotations.scope.ActivityScope

@Module
class PicassoModule {

    @[Provides ActivityScope]
    fun picasso(@ApplicationContext context: Context): Picasso {
        return Picasso.Builder(context).build()
    }
}