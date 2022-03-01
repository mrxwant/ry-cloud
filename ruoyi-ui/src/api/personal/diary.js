import request from '@/utils/request'

// 查询日记列表
export function listDiary(query) {
  return request({
    url: '/personal/diary/list',
    method: 'get',
    params: query
  })
}

// 查询日记详细
export function getDiary(id) {
  return request({
    url: '/personal/diary/' + id,
    method: 'get'
  })
}

// 新增日记
export function addDiary(data) {
  return request({
    url: '/personal/diary',
    method: 'post',
    data: data
  })
}

// 修改日记
export function updateDiary(data) {
  return request({
    url: '/personal/diary',
    method: 'put',
    data: data
  })
}

// 删除日记
export function delDiary(id) {
  return request({
    url: '/personal/diary/' + id,
    method: 'delete'
  })
}

// 导出日记
export function exportDiary(query) {
  return request({
    url: '/personal/diary/export',
    method: 'get',
    params: query
  })
}

export function getTypes(queryString) {
  return request({
    url: '/personal/diary/labelList?queryString='+queryString,
    method: 'get'
  })
}
