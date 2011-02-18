package com.menethil.logmylife.main;

import java.util.ArrayList;
import java.util.List;

import com.menethil.logmylife.service.IBaseService;
import com.menethil.logmylife.service.WorkService;
import com.menethil.logmylife.task.WorkServiceWraper;
import com.menethil.logmylife.util.LogUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 整个工程的应用上下文环境，初始工程初始化这个类 (包括一些静态变量，和一些全局用的列表数据)
 * 
 * @author LinLin
 */
public class BaseContext {
	private static final String TAG = "exeswarder.main.BaseContext";

	private boolean isInit = false; // 是否初始化完成

	private boolean dataBaseOk = false; // 系统启动数据是否初始化完成

	// 本类的单态实例
	private static BaseContext baseContext = null;
	// 全局的上下文Context
	private Context appContext = null;

	// 全局的服务处理类
	private IBaseService baseService = null;
	// 每个服务业务的包装者类，通过他来调用服务业务类，返回的数据会被封装好
	private WorkServiceWraper serviceWarper = null;

	// 全局处理器, 由receiver获得引用
	public MarketBaseHandler mHandler;

	// ///////////////////////// 全局常量 ////////////////////////////////
	// 软件卸载成功后触发
	public static final int M_QQONLINE = 0;
	// 消息终止
	public static final int M_IPADDRESS = 1;

	private BaseContext() {

	}

	/**
	 * 单例构造模式
	 * 
	 * @return baseContext
	 */
	public synchronized static BaseContext getInstance() {
		if (baseContext == null) {
			baseContext = new BaseContext();
		}
		return baseContext;
	}

	/**
	 * 初始化这个类,把全局的东西全部加载进来
	 * 
	 * @param appContext
	 */
	public synchronized void initBaseContext(Context appContext) {
		if (isInit == true) {
			return;
		}

		LogUtil.w(TAG, "################## BaseContext is initing...");

		// 全局context
		this.appContext = appContext;

		baseService = new WorkService(appContext);
		serviceWarper = new WorkServiceWraper(baseService);
		// 处理器
		mHandler = new MarketBaseHandler(appContext.getMainLooper());

		initDataBase(appContext);
		if (dataBaseOk == false) {
			return;
		}

		// 最后成功了以后把isInit设成true
		isInit = true;
	}

	/**
	 * 初始化本应用程序数据
	 */
	public void initDataBase(Context appContext) {
		dataBaseOk = true;
	}

	public boolean isInit() {
		return isInit;
	}

	public WorkServiceWraper getServiceWarper() {
		return serviceWarper;
	}

	public void setServiceWarper(WorkServiceWraper serviceWarper) {
		this.serviceWarper = serviceWarper;
	}

	public Context getAppContext() {
		return appContext;
	}

	/**
	 * 处理消息
	 * 
	 * @param msg
	 */
	public void handleMarketMessage(Message msg) {
		if (mHandler != null) {
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * @return the mHandler
	 */
	public void registerSubHandler(MarkableHandler handler) {
		mHandler.registeHandler(handler);
	}

	/**
	 * 务必相关部分在推出的时候取消自己的注册
	 */
	public void unregisterSubHandler(MarkableHandler handler) {
		mHandler.unregistHandler(handler);
	}

	// 主处理器
	// 对应一个Message arg1: 标识这个消息与那个handler对应 arg1:为默认的0时候表示一个未被标记的消息，这是所有的handler
	// 都会处理，MarketBaseHandler
	// 会处理需要的消息而不管arg1；arg2:标示为Constants.MESSAGE_END标示这个消息不需要
	// 在由后续处理而来。
	class MarketBaseHandler extends Handler {
		// 关联的子级handler，子集倒序(task等不activity先处理)的任务是完成视图部分，主handle完成数据部分的统一处理。
		// 这里将该为适应弱引用,
		// TODO 要修改，实现弱引用
		private List<MarkableHandler> weakList = new ArrayList<MarkableHandler>();

		public MarketBaseHandler(Looper looper) {
			super(looper);
		}

		void registeHandler(MarkableHandler handler) {
			weakList.add(0, handler);
			LogUtil.d(TAG, "registeHandler now size: " + weakList.size());
		}

		void unregistHandler(MarkableHandler handler) {
			weakList.remove(handler);
			LogUtil.d(TAG, "unregistHandler now " + "size: " + weakList.size());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			LogUtil.d(TAG, "handleMessage: " + msg);
			switch (msg.what) {
			// 卸载软件
			case M_QQONLINE:
				handleAppUNInstalled(msg);
				break;

			}

			// 传递给子级
			for (MarkableHandler handler : weakList) {
				// arg1为0表示一个未被标记的任务
				if (handler.isHandleAll() || msg.arg1 == 0
						|| handler.getMssageMark() == msg.arg1
						|| handler.getDefaultMessageMark() == msg.arg1) {
					// 消息是否终止
					if (msg.arg2 != M_IPADDRESS) {
						handler.handleMessage(msg); // 注意这里是同步进行，不使用sendMessage();
					}
				}
			}

		}

		// 处理软件卸载成功
		private void handleAppUNInstalled(Message msg) {
			String pname = (String) msg.obj;

			// 这里写处理的代码

			// 包名生成一个默认的标记
			// msg.arg1 = (pname).hashCode();

			LogUtil.d(TAG, "handleAppUNInstalled pname = " + pname);
		}

	}

}
