package com.app.tools.common.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 認証の種別の定数.
 *
 */
public enum AuthenticationRollType {

	ADMIN_ROLL("admin", "管理者"),

	USER_ROLL("user", "ユーザ");

    private static final Map<String, AuthenticationRollType> KEY_MAP = Collections.unmodifiableMap(createKeyMap());

	private static final Map<String, AuthenticationRollType> VALUE_MAP = Collections.unmodifiableMap(createValueMap());

    private final String key;

    private final String value;

    private AuthenticationRollType(String key, String value) {
    	this.key = key;
        this.value = value;
    }

    /**
     * 物理名での値を取得する。
     *
     * @return　物理値
     */
    public String getKey() {
    	return this.key;
    }

    /**
     * 論理名での値を取得する。
     *
     * @return 論理値
     */
    public String getValue() {
        return this.value;
    }

    private static Map<String, AuthenticationRollType> createKeyMap() {
    	AuthenticationRollType[] types = AuthenticationRollType.values();
        Map<String, AuthenticationRollType> map = new HashMap<String, AuthenticationRollType>(types.length);
        for (AuthenticationRollType type : types) {
            map.put(type.getKey(), type);
        }

        return map;
    }

    private static Map<String, AuthenticationRollType> createValueMap() {
    	AuthenticationRollType[] types = AuthenticationRollType.values();
        Map<String, AuthenticationRollType> map = new HashMap<String, AuthenticationRollType>(types.length);
        for (AuthenticationRollType type : types) {
            map.put(type.getValue(), type);
        }

        return map;
    }

    /**
     * <code>key</code>に該当する<code>AuthenticationRollType</code>オブジェクトを取得する。
     *
     * @param key 状態値
     * @return AccessPermitStatus
     */
    public static AuthenticationRollType getByKey(String key) {
        if (key == null) {
            return null;
        }

        return KEY_MAP.get(key);
    }

    /**
     * <code>value</code>に該当する<code>AuthenticationRollType</code>オブジェクトを取得する。
     *
     * @param value 状態値
     * @return AccessPermitStatus
     */
    public static AuthenticationRollType getByValue(String value) {
        if (value == null) {
            return null;
        }

        return VALUE_MAP.get(value);
    }
}
