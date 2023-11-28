package me.elb1to.ffa.util.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu {

	private Map<Integer, Button> buttons = new HashMap<>();
	public static Map<String, Menu> currentlyOpenedMenus = new HashMap<>();

	private ItemStack createItemStack(Player player, Button button) {
		ItemStack item = button.getButtonItem(player);
		if (item.getType() != Material.SKULL) {
			ItemMeta meta = item.getItemMeta();
			if (meta != null && meta.hasDisplayName()) {
				meta.setDisplayName(meta.getDisplayName() + "§b§c§d§e");
			}

			item.setItemMeta(meta);
		}

		return item;
	}

	public void openMenu(final Player player) {
		this.buttons = this.getButtons(player);

		Menu previousMenu = Menu.currentlyOpenedMenus.get(player.getName());
		Inventory inventory = null;
		int size = this.getSize() == -1 ? this.size(this.buttons) : this.getSize();
		boolean update = false;
		String title = this.getTitle(player);

		if (title.length() > 32) {
			title = title.substring(0, 32);
		}

		if (player.getOpenInventory() != null) {
			if (previousMenu == null) {
				player.closeInventory();
			} else {
				int previousSize = player.getOpenInventory().getTopInventory().getSize();

				if (previousSize == size && player.getOpenInventory().getTopInventory().getTitle().equals(title)) {
					inventory = player.getOpenInventory().getTopInventory();
					update = true;
				} else {
					player.closeInventory();
				}
			}
		}

		if (inventory == null) {
			inventory = Bukkit.createInventory(player, size, title);
		}

		inventory.setContents(new ItemStack[inventory.getSize()]);
		currentlyOpenedMenus.put(player.getName(), this);

		for (Map.Entry<Integer, Button> buttonEntry : this.buttons.entrySet()) {
			inventory.setItem(buttonEntry.getKey(), createItemStack(player, buttonEntry.getValue()));
		}

		if (update) {
			player.updateInventory();
		} else {
			player.openInventory(inventory);
		}

		this.onOpen(player);
	}

	public int size(Map<Integer, Button> buttons) {
		int highest = 0;

		for (int buttonValue : buttons.keySet()) {
			if (buttonValue > highest) {
				highest = buttonValue;
			}
		}

		return (int) (Math.ceil((highest + 1) / 9D) * 9D);
	}

	public void updateInventory(Player player) {
		Inventory inventory = player.getOpenInventory().getTopInventory();
		inventory.setContents(new ItemStack[inventory.getSize()]);

		currentlyOpenedMenus.put(player.getName(), this);

		for (Map.Entry<Integer, Button> buttonEntry : getButtons(player).entrySet()) {
			inventory.setItem(buttonEntry.getKey(), createItemStack(player, buttonEntry.getValue()));
		}
		player.updateInventory();
	}

	public static Map<String, Menu> getCurrentlyOpenedMenus() {
		return currentlyOpenedMenus;
	}

	public static void setCurrentlyOpenedMenus(Map<String, Menu> currentlyOpenedMenus) {
		Menu.currentlyOpenedMenus = currentlyOpenedMenus;
	}

	public Map<Integer, Button> getButtons() {
		return buttons;
	}

	public void setButtons(Map<Integer, Button> buttons) {
		this.buttons = buttons;
	}

	public int getSlot(int x, int y) {
		return ((9 * y) + x);
	}

	public int getSize() {
		return -1;
	}

	public abstract String getTitle(Player player);

	public abstract Map<Integer, Button> getButtons(Player player);

	public void onOpen(Player player) {

	}

	public void onClose(Player player) {

	}

	public void fillEmptySlots(Map<Integer, Button> buttons, ItemStack itemStack) {
		int bound = getSize();

		for (int slot = 0; slot < bound; slot++) {
			if (buttons.get(slot) == null) {
				buttons.put(slot, new Button() {
					@Override
					public ItemStack getButtonItem(Player player) {
						return itemStack;
					}
				});
			}
		}
	}
}