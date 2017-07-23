package base;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * <pre>
 *     @author sin
 *     @date 2017-07-04
 *     @desc 封装的ViewHolder基类
 * </pre>
 */

public abstract class ViewHolder<T> {

    protected View rootView;

    public ViewHolder(View root){
        rootView = root;
    }

    protected View findViewById(@IdRes int id){
        return rootView.findViewById(id);
    }

    protected View getRootView(){
        return rootView;
    }

    public abstract void bindData(T t);

}
