package cn.itsite.ui.base;

import android.content.Context;
import android.view.View;

/**
 * @项目名: IT站点
 * @包名: cn.site
 * @类名: NewsListController
 * @创建者: 李勇
 * @创建时间: 2016-2-11 上午10:11:41
 * @描述:新闻数据list页面对应的controller
 * 
 */

public abstract class BaseController {

	public View mRootView;
	protected Context mContext;

	public BaseController(Context context) {
		this.mContext = context;
		this.mRootView = initView();
	}

	/**
	 * 初始化View
	 * 
	 * @return
	 */
	protected abstract View initView();

	/**
	 * 初始化数据的方法，孩子如果有数据初始化，就复写
	 */
	public void initData() {

	}

}
