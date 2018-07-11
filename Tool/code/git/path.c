void *strip_path_suffix(const char *path, const char *suffix)
{
	while (suffix_len) {
		if (!path_len)
			return NULL;

		else if (path[--path_len] != suffix[--suffix_len])
			return NULL;
	}

}