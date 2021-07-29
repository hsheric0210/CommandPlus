package com.eric0210.commandplus.utils;

import org.bukkit.ChatColor;

public final class StringPool
{
	private StringPool()
	{
	}

	public static final String E_PLAYER_NOT_FOUND = ChatColor.DARK_RED + "플레이어를 찾을 수 없습니다.";
	public static final String E_FAILED_TO_PARSE_POSITION = ChatColor.DARK_RED + "위치 파싱에 실패했습니다.";
	public static final String E_EXECUTION_POSITION_NOT_RECOGNIZED = ChatColor.DARK_RED + "커맨드 블록이나 플레이어만이 실행할 수 있습니다.";
	public static final String E_ANALOG_OUTPUT_REQUIRED = ChatColor.DARK_RED + "아날로그 출력(레드스톤 비교기)을(를) 가진 커맨드 블록이나 플레이어만이 실행할 수 있습니다.";
	public static final String E_ITEM_TYPE_NOT_RECOGNIZED = ChatColor.DARK_RED + "%s는 존재하지 않는 아이템 타입입니다.";
	public static final String E_ARMOR_SLOT_NOT_RECOGNIZED = ChatColor.DARK_RED + "%s는 지원되지 않는 아머 슬롯입니다.";
	public static final String E_INVENTORY_NOT_FOUND = ChatColor.DARK_RED + "입력한 블록(%d, %d, %d - %s)은(는) 인벤토리를 가지는 블록이 아닙니다.";
	public static final String E_NOT_PLAYER = ChatColor.DARK_RED + "플레이어만 사용 가능합니다.";
	public static final String E_POSITION_NOT_SPECIFIED = ChatColor.DARK_RED + "위치가 지정되지 않았습니다.";
	public static final String E_POSITION_NOT_STORED = ChatColor.RED + "목표 위치가 저장되어 있지 않습니다.";
	public static final String E_BLOCK_NOT_FOUND = ChatColor.DARK_RED + "선택된 블록이 월드 상에 존재하지 않습니다.";
	public static final String E_WORLD_NOT_RECOGNIZED = ChatColor.DARK_RED + "월드를 찾을 수 없습니다. (인-게임 플레이어 또는 커맨드 블록으로 명령을 실행해주세요)";
	public static final String E_CHUNK_COORDINATE_NOT_RECOGNIZED = ChatColor.DARK_RED + "청크 좌표 파싱에 실패했습니다.";
	public static final String E_NEGATIVE_HEALTH = ChatColor.DARK_RED + "체력은 0보다 큰 값이어야 합니다.";
	public static final String E_VEHICLE_TYPE_NOT_RECOGNIZED = ChatColor.DARK_RED + "알 수 없는 이동 수단 종류입니다!";
	public static final String E_FAILED_TO_PARSE_NUMBER = ChatColor.DARK_RED + "%s는 올바른 숫자가 아닙니다!";
}
