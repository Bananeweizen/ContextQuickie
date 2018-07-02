package contextquickie.handlers.tortoise.svn;

import contextquickie.handlers.tortoise.AbstractTortoiseItemInMainMenu;
import contextquickie.preferences.PreferenceConstants;
import contextquickie.tools.Registry;

/**
 * @author ContextQuickie
 *
 *         Property tester which checks if a specific Tortoise SVN entry is part
 *         of the main menu or not. It is used to show/hide the Tortoise add-in
 *         context menu entries.
 */
public class TortoiseSvnItemInMainMenu extends AbstractTortoiseItemInMainMenu
{
  /**
   * A value indicating whether the context menu settings has already been read
   * from the registry or not.
   */
  private static boolean contextMenuSettingsRead;

  /**
   * Value of the registry key ContextMenuEntries.
   */
  private long contextMenuEntries = TortoiseSvnMenuItems.MENUCHECKOUT | TortoiseSvnMenuItems.MENUUPDATE | TortoiseSvnMenuItems.MENUCOMMIT;

  /**
   * Value of the registry key ContextMenuEntriesHigh.
   */
  private long contextMenuEntriesHigh;

  /**
   * Default constructor.
   * 
   */
  public TortoiseSvnItemInMainMenu()
  {
    super(PreferenceConstants.TORTOISE_SVN);
  }

  @Override
  protected final boolean isEntryInMainMenu(final String entry)
  {
    final long int32BitMaxValue = 0xFFFFFFFFL;
    long entryValue = 0;
    final long compareValue;
    boolean result = false;
    Exception reflectionException = null;
    try
    {
      entryValue = TortoiseSvnMenuItems.class.getDeclaredField(entry).getLong(null);
    }
    catch (IllegalArgumentException e)
    {
      reflectionException = e;
    }
    catch (IllegalAccessException e)
    {
      reflectionException = e;
    }
    catch (NoSuchFieldException e)
    {
      reflectionException = e;
    }
    catch (SecurityException e)
    {
      reflectionException = e;
    }

    if (reflectionException != null)
    {
      reflectionException.printStackTrace();
    }
    else
    {
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
    }

    return result;
  }

  @Override
  protected final void readSettingsFromRegistry()
  {
    if (contextMenuSettingsRead == false)
    {
      contextMenuSettingsRead = true;
      final String registryLocation = "HKEY_CURRENT_USER\\Software\\TortoiseSVN";
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
}