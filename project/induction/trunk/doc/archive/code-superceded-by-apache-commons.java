	/**
	 * The object of this method is to faciliate removing directory, but with
	 * reasonable safelty measure. If the method suceeds the directory pointed by
	 * oParentDir will be removed. If oParentDir is directory is empty it is removed
	 * right away, otherwise this method recursively deletes the files in oParentDir
	 * that match oFileFilter, after this recursively file delete, any resulting
	 * empty subfolders are recursively deleted, if after this step oParentDir is empty
	 * the oParentDir is deleted.
	 *
	 * Recursively deletes the files matching the specified wildcard in the path pointed by the
	 *
	 * @param oParentDir
	 * @param oFileFilter
	 * @return
	 * @throws IOException
	 */
	public static boolean removeDirectory( File oParentDir, FileFilter oFileFilter )
	{
		// TODO: implement

		return false;
	}

