package com.menethil.logmylife.task;

import com.cooler.schema.service.IService;
import com.cooler.schema.task.AServiceWraper;
import com.cooler.schema.task.AsyncOperation;
import com.cooler.schema.task.IResultReceiver;
import com.cooler.schema.task.mark.ATaskMark;
import com.cooler.schema.task.tracker.AInvokeTracker;
import com.menethil.logmylife.main.BaseContext;
import com.menethil.logmylife.tracker.WebServerTracker;

public class WorkServiceWraper extends AServiceWraper {
	public static final String TAG = "MarketServiceWraper";
	private IService service;

	public WorkServiceWraper(IService service) {
		super(service);
		// TODO Auto-generated constructor stub
		this.service = service;
	}

	@SuppressWarnings("unused")
	private BaseContext baseContext = BaseContext.getInstance();

	/**
	 * 请求一个 WebService
	 * 
	 * @param resultReceiver
	 * @param taskMark
	 * @param attach
	 * @return
	 */
	public AsyncOperation CallWebService(IResultReceiver resultReceiver,
			ATaskMark taskMark, Object attach) {

		AsyncOperation operation = null;
		AInvokeTracker webServerTracker = new WebServerTracker(resultReceiver);
		operation = wraperOperationLocal(resultReceiver, taskMark, attach,
				"CallWebService", webServerTracker);

		return operation;
	}

	/**
	 * 方法与不定参数必须匹配
	 * 
	 * @param resultReceiver
	 * @param taskMark
	 * @param attach
	 * @param wraperMethodName
	 * @param tracker
	 * @param args
	 * @return
	 */
	private AsyncOperation wraperOperationLocal(IResultReceiver resultReceiver,
			ATaskMark taskMark, Object attach, String wraperMethodName,
			AInvokeTracker tracker, Object... args) {
		try {
			AsyncOperation operation = null;
			if (AsyncOperation.isTaskExist(taskMark)) {
				operation = takeoverExistTask(resultReceiver, taskMark);
			} else {
				operation = wraperOperation(tracker, taskMark,
						wraperMethodName, attach);
				operation.excuteOperate(service, taskMark);
			}
			return operation;
		} catch (Exception e) {
			return null;
		}
	}
}