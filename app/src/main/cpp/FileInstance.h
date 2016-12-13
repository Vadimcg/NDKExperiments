//
// Created by vadimcg on 13.12.16.
//

#ifndef NDKEXPERIMENTS_FILEINSTANCE_H
#define NDKEXPERIMENTS_FILEINSTANCE_H

#import <string>

class FileInstance {

private:

        int size_;
        std::string name_;
        std::string path_;

public:

    FileInstance(std::string& path_);
    FileInstance(const FileInstance& value);
    FileInstance(FileInstance&& value);

    ~FileInstance();

    int getSize();
    std::string getName();

};


#endif //NDKEXPERIMENTS_FILEINSTANCE_H
