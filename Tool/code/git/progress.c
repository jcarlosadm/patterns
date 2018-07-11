static int display(struct progress *progress, unsigned n, const char *done)
{
	if (progress->delay) {
		if (!progress_update || --progress->delay)
			return 0;
	}
	return 0;
}