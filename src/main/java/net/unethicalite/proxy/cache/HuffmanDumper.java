package net.unethicalite.proxy.cache;

import net.runelite.cache.IndexType;
import net.runelite.cache.fs.Archive;
import net.runelite.cache.fs.ArchiveFiles;
import net.runelite.cache.fs.FSFile;
import net.runelite.cache.fs.Index;
import net.runelite.cache.fs.Storage;
import net.runelite.cache.fs.Store;

import java.io.IOException;

public class HuffmanDumper {
    private final Store store;

    public HuffmanDumper(Store store) {
        this.store = store;
    }

    public byte[] load() throws IOException {
        Storage storage = store.getStorage();
        Index index = store.getIndex(IndexType.BINARY);
        Archive archive = index.findArchiveByName("huffman");

        byte[] archiveData = storage.loadArchive(archive);
        ArchiveFiles files = archive.getFiles(archiveData);

        for (FSFile f : files.getFiles()) {
            return f.getContents();
        }

        return null;
    }
}
