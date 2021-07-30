package com.eric0210.commandplus.utils;

import org.bukkit.ChatColor;

public final class StringPool
{
	private StringPool()
	{
	}

	public static final String PLUGIN_PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + ChatColor.BOLD + "Command" + ChatColor.RESET + ChatColor.AQUA + ChatColor.BOLD + "+" + ChatColor.RESET + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;
	public static final String PLUGIN_PREFIX_WITHOUT_COLOR = "[Command+] ";

	// Error messages
	public static final String ERROR_PREFIX = PLUGIN_PREFIX + ChatColor.DARK_RED + ChatColor.BOLD + "오류" + ChatColor.GRAY + ": " + ChatColor.RESET + ChatColor.DARK_RED;

	public static final String E_PLAYER_NOT_FOUND = ERROR_PREFIX + "플레이어를 찾을 수 없습니다.";
	public static final String E_INVENTORY_NOT_FOUND = ERROR_PREFIX + "입력한 블록(%1$d, %2$d, %3$d - %4$s)은(는) 인벤토리를 가지는 블록이 아닙니다.";
	public static final String E_BLOCK_NOT_FOUND = ERROR_PREFIX + "선택된 블록이 월드 상에 존재하지 않습니다.";
	public static final String E_WORLD_NOT_FOUND = ERROR_PREFIX + "월드를 찾을 수 없습니다! (플레이어 또는 커맨드 블록으로 명령을 실행해주세요)";
	public static final String E_EXECUTION_POSITION_NOT_FOUND = ERROR_PREFIX + "커맨드 블록이나 플레이어만이 실행할 수 있습니다.";
	public static final String E_UNKNOWN_ITEM_TYPE = ERROR_PREFIX + "'%1$s'는 존재하지 않는 아이템 타입입니다!";
	public static final String E_UNKNOWN_ARMOR_SLOT = ERROR_PREFIX + "'%1$s'는 지원되지 않는 아머 슬롯입니다!";
	public static final String E_UNKNOWN_VEHICLE_TYPE = ERROR_PREFIX + "알 수 없는 이동 수단 종류입니다!";
	public static final String E_POSITION_FORMAT_EXCEPTION = ERROR_PREFIX + "위치 파싱에 실패했습니다!";
	public static final String E_CHUNK_COORDINATE_FORMAT_EXCEPTION = ERROR_PREFIX + "청크 좌표 파싱에 실패했습니다! 숫자를 제대로 적었는지 확인해주세요.";
	public static final String E_NUMBER_FORMAT_EXCEPTION = ERROR_PREFIX + "'%1$s'는 올바른 숫자가 아닙니다!";
	public static final String E_ANALOG_OUTPUT_REQUIRED = ERROR_PREFIX + "아날로그 출력(레드스톤 비교기)을(를) 가진 커맨드 블록이나 플레이어만이 실행할 수 있습니다!";
	public static final String E_NOT_PLAYER = ERROR_PREFIX + "플레이어만 사용 가능한 명령어입니다! 게임에 로그인하여 사용해주세요.";
	public static final String E_NEGATIVE_HEALTH = ERROR_PREFIX + "체력은 반드시 0보다 큰 값이어야 합니다!";
	public static final String E_POSITION_NOT_SPECIFIED = ERROR_PREFIX + "위치가 지정되지 않았습니다.";
	public static final String E_POSITION_NOT_STORED = ChatColor.RED + "목표 위치가 저장되어 있지 않습니다.";
	public static final String E_COMMAND_ALREADY_BANNED = ChatColor.RED + "이 명령어는 이미 사용 금지 조치되어 있습니다!";
	public static final String E_COMMAND_NOT_BANNED = ChatColor.RED + "이 명령어는 사용 금지 조치되어 있지 않습니다!";

	public static final String SUCCESS_PREFIX = PLUGIN_PREFIX + ChatColor.GREEN + ChatColor.BOLD + "성공" + ChatColor.GRAY + ": " + ChatColor.RESET + ChatColor.GREEN;
	public static final String FAILED_PREFIX = PLUGIN_PREFIX + ChatColor.RED + ChatColor.BOLD + "실패" + ChatColor.GRAY + ": " + ChatColor.RESET + ChatColor.RED;

	// BanCommand
	public static final String BANCOMMAND_RESPONCE = SUCCESS_PREFIX + "명령어 '%1$s'를 사용 금지 조치 시켰습니다.";

	// Chunkload
	public static final String CHUNKLOAD_LOADED_CHUNK = SUCCESS_PREFIX + "청크 (%1$d ~ %2$d, %3$d ~ %4$d) 를 로드하였습니다.";
	public static final String CHUNKLOAD_UNLOADED_CHUNK = SUCCESS_PREFIX + "청크 (%1$d ~ %2$d, %3$d ~ %4$d) 를 언로드하였습니다.";
	public static final String CHUNKLOAD_PROTECTION_STARTED = SUCCESS_PREFIX + "청크 (%1$d ~ %2$d, %3$d ~ %4$d) 를 언로드로부터 보호하기 시작하였습니다.";
	public static final String CHUNKLOAD_PROTECTION_STOPPED = SUCCESS_PREFIX + "청크 (%1$d ~ %2$d, %3$d ~ %4$d) 를 언로드로부터 보호하는 것을 중지하였습니다.";
	public static final String CHUNKLOAD_REFRESHED_CHUNK = SUCCESS_PREFIX + "청크 (%1$d ~ %2$d, %3$d ~ %4$d) 를 리프레시하였습니다.";

	// Compass Target
	public static final String COMPASSTARGET_CHANGED_LOCATION = SUCCESS_PREFIX + "%1$s(들)의 나침반 목표 위치를 %2$s (으)로 바꾸었습니다.";

	// Damage Dealt
	public static final String DAMAGEDEALT_CHANGED = SUCCESS_PREFIX + "%1$s(들)의 주는 데미지를 %2$f.3%%(으)로 조정했습니다.";

	// Damage Took
	public static final String DAMAGETOOK_CHANGED = SUCCESS_PREFIX + "%1$s(들)의 받는 데미지를 %2$f.3%%(으)로 조정했습니다.";

	// Dismount
	public static final String DISMOUNT_PLAYER_EJECTION = SUCCESS_PREFIX + "%1$s을(를) 해당 플레이어가 타고 있던 이동 수단에게서 강제로 내쫒았습니다.";
	public static final String DISMOUNT_VEHICLE_REMOVED = SUCCESS_PREFIX + "%1$s가 타고 있던 이동 수단은 제거되었습니다.";

	// Flight Allowed
	public static final String FLIGHTALLOWED_ENABLED = SUCCESS_PREFIX + "%1$s(들)은 이제 날 수 있습니다.";
	public static final String FLIGHTALLOWED_DISABLED = SUCCESS_PREFIX + "%1$s(들)은 이제 날 수 없습니다.";

	// Flying
	public static final String FLYING_ENABLED = SUCCESS_PREFIX + "%1$s(들)은 이제 날고 있습니다.";
	public static final String FLYING_DISABLED = SUCCESS_PREFIX + "%1$s(들)은 이제 날고 있지 않습니다.";

	// Max Health
	public static final String MAXHEALTH_CHANGED = SUCCESS_PREFIX + "%1$s(들)의 최대 체력을 %2$f.3로 조정했습니다.";

	// Food Level
	public static final String FOODLEVEL_CHANGED = SUCCESS_PREFIX + "%1$s(들)의 배고픔 수치를 %2$d로 조정했습니다.";

	// Mount
	public static final String MOUNT_MOUNTED = SUCCESS_PREFIX + "%1$s을(를) %2$s에 탑승시켰습니다.";

	// Open Inventory
	public static final String OPENINV_OPENED = SUCCESS_PREFIX + "플레이어 %1$s에 대해 (%2$s) 에 있는 블록의 인벤토리를 열었습니다.";

	// GetTarget
	public static final String GETTARGET = SUCCESS_PREFIX + "저장된 위치" + ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + ChatColor.BOLD + "%1$s" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "= " + ChatColor.DARK_GRAY + "[(" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "%2$s" + ChatColor.DARK_GRAY + ")" + ChatColor.GRAY + ", block=" + ChatColor.DARK_GRAY + "(" + ChatColor.AQUA + "%3$d, $4$d, %5$d" + ChatColor.DARK_GRAY + ")" + ChatColor.GRAY + ", chunk=" + ChatColor.DARK_GRAY + "(" + ChatColor.BLUE + "%6$d, %7$d" + ChatColor.DARK_GRAY + ")" + SUCCESS_PREFIX + "]";

	// Target
	public static final String TARGET = SUCCESS_PREFIX + "위치 (%1$s) 이(가) '%2$s'(이)라는 이름으로 저장되었습니다.";

	// Tp+
	public static final String TELEPORTPLUS_SUCCESS = SUCCESS_PREFIX + "%1$s을(를) 성공적으로 %2$s으로 순간이동시켰습니다.";

	// TestForHeldItem
	public static final String TESTFORHELDITEM_FOUND = SUCCESS_PREFIX + "%1$s(들)의 손에서 입력된 아이템을 총 %2$d개 찾았습니다.";
	public static final String TESTFORHELDITEM_NOT_FOUND = FAILED_PREFIX + "%1$s(들)의 손에서 입력된 아이템을 찾지 못했습니다.";

	// TestForItem
	public static final String TESTFORITEM_ENTITY_FOUND = SUCCESS_PREFIX + "%1$s(들)에게서 입력된 아이템을 총 %2$d개 찾았습니다.";
	public static final String TESTFORITEM_ENTITY_NOT_FOUND = FAILED_PREFIX + "%1$s(들)에게서 입력된 아이템을 찾지 못했습니다.";
	public static final String TESTFORITEM_BLOCK_FOUND_COUNT = SUCCESS_PREFIX + "%1$s(%2$s)의 인벤토리에서 입력된 아이템을 총 %3$d개 찾았습니다.";
	public static final String TESTFORITEM_BLOCK_NOT_FOUND = FAILED_PREFIX + "%1$s(%2$s)의 인벤토리에서 입력된 아이템을 찾지 못했습니다.";
	public static final String TESTFORITEM_BLOCK_FOUND = SUCCESS_PREFIX + "%1$s(%2$s)의 인벤토리에서 입력된 아이템을 찾았습니다.";

	// Velocity
	public static final String VELOCITY_APPLIED = SUCCESS_PREFIX + "%1$s에게 %2$s 만큼의 가속을 주었습니다.";

	// UnbanCommand
	public static final String UNBANCOMMAND_RESPONCE = SUCCESS_PREFIX + "명령어 '%1$s'의 사용 금지 조치를 해제하였습니다.";
}
