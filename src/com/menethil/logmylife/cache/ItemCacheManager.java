package com.menethil.logmylife.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.cooler.schema.task.mark.ATaskMark;
import com.menethil.logmylife.bean.BaseInfo;
import com.menethil.logmylife.task.mark.WebServiceTaskMark;
import com.menethil.logmylife.util.LogUtil;

/**
 * 2010-6-8 <br>
 * 因为视图一般最多显示10个左右的应用, <br>
 * 所有如果缓存 中对应类型且符合查询条件的应用的格式>=10的时候直接就从缓存 中取得，<br>
 * 后续的查询在去服务器查询。这里提供的时候文本类别的缓存。
 * 
 * @author ckcs <br>
 * 
 */
public final class ItemCacheManager {

	public static final String TAG = "ItemCacheManager";

	public static final boolean logCache = false;

	// 维持添加的顺序
	// key: 那种类型数据的缓存 value: 对应此类别的数据id
	private Map<ATaskMark, ArrayList<Integer>> recordIdMapCache;

	// 数据缓存
	// key: 数据id(数据库唯一id) value: 数据
	// appTypeCache size 不一定等于 appItemCache 的size
	private Map<Integer, BaseInfo> recordItemMapCache;

	// 软件分类缓存
	private Map<Integer, WebServiceTaskMark> categoryTypeMapCache;

	public ItemCacheManager() {
		recordIdMapCache = new HashMap<ATaskMark, ArrayList<Integer>>();
		recordItemMapCache = new HashMap<Integer, BaseInfo>();
		categoryTypeMapCache = new HashMap<Integer, WebServiceTaskMark>();
	}

	/**
	 * 将软件添加到缓存 物品将从末尾开始添加。<br>
	 * 处于一致性考虑不添加一样的应用！但需要保持加载列表。
	 * 
	 * @param start
	 *            起始索引
	 * @param baseItems
	 * 
	 */
	public void addRecordItemToCache(ATaskMark type,
			ArrayList<? extends BaseInfo> baseItems) {
		if (type == null) {
			throw new IllegalArgumentException("Item type can not null.");
		}

		ArrayList<Integer> idList = getRecordItemIdList(type);
		int id = 0;
		for (BaseInfo baseItem : baseItems) {
			id = baseItem.getId();
			// 不包含是添加到末尾
			if (!idList.contains(id)) {
				idList.add(id);
			}
			// 存在时不覆盖。
			if (!recordItemMapCache.containsKey(id)) {
				recordItemMapCache.put(id, baseItem);
			}
		}
		if (logCache) {
			Log.d(TAG, "addFileItemToCache type: " + type + " count: "
					+ baseItems.size() + " item size: " + idList.size()
					+ " item type: size: " + recordItemMapCache.size());
		}
	}

	/**
	 * 将软件替换旧的软件列表，但软件信息仍然保持。<br>
	 * 出于一致性考虑不添加一样的应用！但需要保持加载列表。<br>
	 * see: 软件的更新
	 * 
	 * @param start
	 *            起始索引
	 * @param recordItems
	 * 
	 */
	public void setRecordItemToCache(ATaskMark type,
			ArrayList<? extends BaseInfo> recordItems) {
		if (type == null) {
			throw new IllegalArgumentException("Item type can not null.");
		}
		ArrayList<Integer> idList = getRecordItemIdList(type);
		// 移除旧的
		idList.clear();
		int id = 0;
		for (BaseInfo baseItem : recordItems) {
			id = baseItem.getId();
			idList.add(id);
			// 存在时不覆盖。
			if (!recordItemMapCache.containsKey(id)) {
				recordItemMapCache.put(id, baseItem);
			}
		}
		if (logCache) {
			Log.d(TAG, "setFileItemToCache type: " + type + " count: "
					+ recordItems.size() + " item size: " + idList.size()
					+ " item type: size: " + recordItemMapCache.size());
		}
	}

	/**
	 * 清除某一类型的文件记录
	 * 
	 * @param type
	 */
	public void clearRecordItemFromCache(ATaskMark type) {
		if (type == null) {
			throw new IllegalArgumentException("Item type can not null.");
		}
		ArrayList<Integer> idList = getRecordItemIdList(type);
		// 移除旧的
		idList.clear();
	}

	/**
	 * 添加一项到缓存
	 * 
	 * @param type
	 *            类型
	 * @param fileItem
	 *            软件
	 */
	public void addRecordItemToCache(ATaskMark type, BaseInfo recordItem) {
		if (type == null) {
			throw new IllegalArgumentException("Item type can not null.");
		}
		ArrayList<Integer> idList = getRecordItemIdList(type);
		int id = recordItem.getId();
		// 不包含是添加到末尾
		if (!idList.contains(id)) {
			idList.add(id);
		}
		// 存在时不覆盖。
		if (!recordItemMapCache.containsKey(id)) {
			recordItemMapCache.put(id, recordItem);
		}
		if (logCache) {
			Log.d(TAG, "addFileItemToCache type: " + type + " fileItem: "
					+ recordItem + " item size: " + idList.size()
					+ " item type: size: " + recordItemMapCache.size());
		}
	}

	/**
	 * 添加一项到缓存
	 * 
	 * @param recordItem
	 *            软件
	 */
	public void addRecordItemToCache(BaseInfo recordItem) {
		int id = recordItem.getId();
		// 存在时不覆盖。
		if (!recordItemMapCache.containsKey(id)) {
			recordItemMapCache.put(id, recordItem);
		}
		if (logCache) {
			Log.d(TAG, "addFileItemToCache fileItem:" + recordItem + " size:"
					+ recordItemMapCache.size());
		}
	}

	/**
	 * 删除指定类型的应用，注意这里的实现是只删除id列表<br>
	 * ，实际的 数据还是存在的。
	 * 
	 * @param type
	 * @param id
	 *            id必须是对象类型
	 */
	public void deleteRecordItemIndexFromCache(ATaskMark type,
			BaseInfo recordItem) {
		List<Integer> idList = getRecordItemIdList(type);
		idList.remove(new Integer(recordItem.getId()));
		if (logCache) {
			Log.d(TAG, "deleteFileItemIndexFromCache type: " + type
					+ " fileItem: " + recordItem);
		}
	}

	/**
	 * 获得缓存中知道索引的软件
	 * 
	 * @param type
	 *            具体类型
	 * @param index
	 *            当前显示的所有，地产数据所有与view层一致
	 * @return
	 */
	public BaseInfo getRecordItemByMarkIndex(ATaskMark type, int index) {
		if (logCache) {
			Log.d(TAG, "getRecordItemByMarkIndex type " + type + " index "
					+ index);
		}
		if (type == null) {
			throw new IllegalArgumentException("Item type can not null.");
		}
		List<Integer> idlist = getRecordItemIdList(type);
		if (index >= idlist.size()) {
			return null;

		} else {
			return recordItemMapCache.get(idlist.get(index));
		}
	}

	/**
	 * 某类型的应用是否存在
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	public boolean isRecordItemInCache(ATaskMark type, BaseInfo recordItem) {
		if (logCache) {
			Log.d(TAG, "isFileItemInCache type: " + type + " RecordItem: "
					+ recordItem);
		}

		if (type == null) {
			throw new IllegalArgumentException("item type can not null.");
		}
		List<Integer> idlist = getRecordItemIdList(type);

		return idlist.contains(recordItem.getId());
	}

	/**
	 * 获得对应类别的软件的id列表
	 * 
	 * @param type
	 * @return
	 */
	public ArrayList<Integer> getRecordItemIdList(ATaskMark type) {
		ArrayList<Integer> idList = recordIdMapCache.get(type);
		if (idList == null && type != null) {
			idList = new ArrayList<Integer>();
			recordIdMapCache.put(type, idList);
		}
		if (logCache) {
			Log.d(TAG, "getFileItemIdList type " + type + " size: "
					+ idList.size());
		}
		return idList;
	}

	/**
	 * 获得对应的类型的软件列表
	 * 
	 * @param type
	 * @return
	 */
	public ArrayList<BaseInfo> getRecordItemList(ATaskMark type) {
		List<Integer> appIdList = getRecordItemIdList(type);
		ArrayList<BaseInfo> baseItemList = new ArrayList<BaseInfo>();
		for (Integer appId : appIdList) {
			baseItemList.add(recordItemMapCache.get(appId));
		}
		if (logCache) {
			Log.d(TAG, "getRecordItemList type " + type + " size: "
					+ appIdList.size());
		}
		return baseItemList;
	}

	/**
	 * 获得知道类型的软件的缓存大小
	 * 
	 * @param itemId
	 *            应用的id
	 * */
	public BaseInfo getRecordItemById(int itemId) {
		if (logCache) {
			Log.d(TAG, "getFileItemById id " + itemId);
		}
		return recordItemMapCache.get(itemId);
	}

	/**
	 * 获得知道类型的软件的缓存大小
	 * 
	 * @param type
	 *            具体类型
	 * */
	public int getRecordItemCount(ATaskMark type) {
		if (type == null) {
			return 0;
		}
		int count = getRecordItemIdList(type).size();
		if (logCache) {
			LogUtil.d(TAG, "getRecordItemCount type " + type + " count "
					+ count);
		}
		return count;
	}

	/**
	 * 类别缓存
	 * 
	 * @return
	 */
	public Map<Integer, WebServiceTaskMark> getCategoryTypeMapCache() {
		return categoryTypeMapCache;
	}

	/**
	 * 设置类别缓存
	 * 
	 * @param categoryTypeMapCache
	 */
	public void setCategoryTypeMapCache(
			Map<Integer, WebServiceTaskMark> categoryTypeMapCache) {
		this.categoryTypeMapCache = categoryTypeMapCache;
	}

	/**
	 * 清空缓存<br>
	 * 缓存的清空必须设计任务的初始化。
	 */
	public void reinitRecordCache() {
		// 文件id列表
		recordIdMapCache.clear();
		// 文件信息
		recordItemMapCache.clear();
		// 文件类别
		categoryTypeMapCache.clear();
	}
}
