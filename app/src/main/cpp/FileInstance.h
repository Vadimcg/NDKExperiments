#import <algorithm>
#import <string>


#ifndef NDKEXPERIMENTS_FILEINSTANCE_H
#define NDKEXPERIMENTS_FILEINSTANCE_H


class FileInstance {

private:

    int size_;
    std::string name_;
    std::string path_;

    std::string getFileNameFromPath(const std::string& filePath);

public:

    FileInstance(std::string& path_);
    FileInstance(const FileInstance& value);
    FileInstance(FileInstance&& value);

    ~FileInstance();

    int getSize();
    std::string getName();

};


#endif //NDKEXPERIMENTS_FILEINSTANCE_H
