package contextquickie.handlers.tortoise.git;

import contextquickie.handlers.tortoise.AbstractTortoiseItemInMainMenu;
import contextquickie.preferences.PreferenceConstants;
import contextquickie.tools.Registry;

/**
 * @author ContextQuickie
 *
 *         Property tester which checks if a specific Tortoise Git entry is part
 *         of the main menu or not. It is used to show/hide the Tortoise add-in
 *         context menu entries.
 */
public class TortoiseGitItemInMainMenu extends AbstractTortoiseItemInMainMenu
{
  /**
   * Value of the registry key ContextMenuEntries.
   */
  private long contextMenuEntries = TortoiseGitMenuItems.MENUCREATEREPOS | TortoiseGitMenuItems.MENUSYNC | TortoiseGitMenuItems.MENUCOMMIT;

  /**
   * Value of the registry key ContextMenuEntriesHigh.
   */
  private long contextMenuEntriesHigh = TortoiseGitMenuItems.MENUCLONE;

  /**
   * Default constructor.
   * 
   */
  public TortoiseGitItemInMainMenu()
  {
    super(PreferenceConstants.TORTOISE_GIT);
  }

  @Override
  protected final boolean isEntryInMainMenu(final String entry)
  {
    final long int32BitMaxValue = 0xFFFFFFFFL;
    long entryValue;
    final long compareValue;
    boolean result = false;
    try
    {
      entryValue = TortoiseGitMenuItems.class.getDeclaredField(entry).getLong(null);
    }
    catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return false;
    }
    
    if (entryValue > int32BitMaxValue)
    {
      entryValue = entryValue >> Integer.SIZE;
      compareValue = this.contextMenuEntriesHigh;
    }
    else
    {
      compareValue = this.contextMenuEntries;
    }
    
    if ((entryValue & compareValue) != 0)
    {
      result = true;
    }

    return result;
  }

  @Override
  protected final void readSettingsFromRegistry()
  {
    final String registryLocation = "HKEY_CURRENT_USER\\Software\\TortoiseGit";
    String registryValue;

    registryValue = Registry.readKey(registryLocation, "ContextMenuEntries");
    if (registryValue != null)
    {
      this.contextMenuEntries = Long.decode(registryValue);
    }

    registryValue = Registry.readKey(registryLocation, "ContextMenuEntrieshigh");
    if (registryValue != null)
    {
      this.contextMenuEntriesHigh = Long.decode(registryValue);
    }
  }
}
