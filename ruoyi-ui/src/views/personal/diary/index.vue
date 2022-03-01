<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="分类" prop="catagory">
        <el-input
          v-model="queryParams.catagory"
          placeholder="请输入分类"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="天气" prop="weather">
        <el-input
          v-model="queryParams.weather"
          placeholder="请输入天气"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="时间" prop="diaryTime">
        <el-date-picker clearable size="small"
          v-model="queryParams.diaryTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:diary:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:diary:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:diary:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:diary:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getFlush"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="diaryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="分类" align="center" prop="catagory"-->
<!--                       :filters=this.filter.types-->
<!--                       :filter-method="filterType"-->
<!--                       @filter-change="handleFilterChange"-->
<!--      />-->
      <el-table-column label="标题" align="center" prop="title" />
      <el-table-column label="天气" align="center" prop="weather"/>
      <el-table-column label="内容" align="center" prop="content" />
      <el-table-column label="时间" align="center" prop="diaryTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.diaryTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="记录时间" align="center" prop="diaryTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="diaryTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:diary:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:diary:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getFlush"
    />

    <!-- 添加或修改日记对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分类" prop="catagory">
            <el-tag
              :key="index"
              v-for="(tag,index) in form.labels"
              closable
              :disable-transitions="false"
              @close="handleClose(tag)"
            >
              {{tag.labelName}}
            </el-tag>
<!--            <el-input-->
<!--              class="input-new-tag"-->
<!--              -->
<!--              v-model="labelInputValue"-->
<!--              ref="saveTagInput"-->
<!--              size="small"-->
<!--              @keyup.enter.native="handleInputConfirm"-->
<!--              @blur="handleInputConfirm"-->
<!--            />-->
          <el-autocomplete
            v-if="labelInputVisible"
            ref="saveTagInput"
            class="inline-input"
            v-model="labelInputValue"
            :fetch-suggestions="queryLabel"
            placeholder="请输入内容"
            @select="handleInputConfirm"
            @change="handleInputConfirm"
            groupBy="type"
            minChars=2
          ></el-autocomplete>

            <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Label</el-button>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="天气" prop="weather">
          <el-input v-model="form.weather" placeholder="请输入天气" />
        </el-form-item>
        <el-form-item label="内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="事件记录时间" prop="diaryTime">
          <el-date-picker clearable size="small"
            v-model="form.diaryTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择事件记录时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import { listDiary, getDiary, delDiary, addDiary, updateDiary, exportDiary ,getTypes} from "@/api/personal/diary";
import Editor from '@/components/Editor';

export default {
  name: "Diary",
  components: {
    Editor,
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 日记表格数据
      diaryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        weather: null,
        content: null,
        diaryTime: null,
        title: null,
        types: []
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      filter:{
        types:[]   },
      labelList:[],
      tags: [
        { value2: '标签一', type: '' },
        { value: '标签二', type: 'success' },
        { value: '标签三', type: 'info' },
        { value: '标签四', type: 'warning' },
        { value: '标签五', type: 'danger' }
      ],
      labelInputVisible: false,
      labelInputValue: '',

    };
  },
  created() {
    this.getFlush();
  },
  methods: {
    /** 查询日记列表 */
    getFlush() {
    this.getList();
    },
    getList() {
      this.loading = true;
      listDiary(this.queryParams).then(response => {
        this.diaryList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    /** 查询日记列表 */
    getTypes(queryString) {
      getTypes(queryString).then(response => {
        for(let i of response.data){
          i.value = i.labelName //将想要展示的数据作为value
        }
        this.labelList = response.data;
      });
    },

    queryLabel(queryString,cb) {
      getTypes(queryString).then(response => {
        for(let i of response.data){
          i.value = i.labelName //将想要展示的数据作为value
        }
        cb(response.data);
      });

    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        weather: null,
        catagory: null,
        content: null,
        createTime: null,
        updateTime: null,
        diaryTime: null,
        title: null,
        labels:[]
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getFlush();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加日记";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDiary(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改日记";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDiary(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getFlush();
            });
          } else {
            addDiary(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getFlush();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除日记编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDiary(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有日记数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportDiary(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    /** 过滤分类 */
    filterType(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    },
    handleFilterChange(){
      alert(123);
    },
    handleInputConfirm() {

      let inputValue = this.labelInputValue;
      if (inputValue) {

        // 去重校验
        for (let i = 0; i < this.form.labels.length; i++) {

          if (inputValue == this.form.labels[i].labelName) {
            this.$message({
              message: '数据已存在',
              type: 'warning'
            });
            this.labelInputValue ='';
            return ;
          }
        }

        const newLabel ={
          "labelName":inputValue,
        }
        this.form.labels.push(newLabel);
      }
      this.labelInputVisible = false;
      this.labelInputValue = '';
    },
    showInput() {
      this.labelInputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    handleClose(tag) {
      this.form.labels.splice(this.form.labels.indexOf(tag), 1);
    },

  }


};
</script>
