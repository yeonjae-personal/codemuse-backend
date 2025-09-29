import { mockDomainHandlers } from './domain/domainHandlers'
import { vocaHandlers } from './voca/vocaHandlers'
import {cmcdHandlers} from './cmcd/cmcdHandlers'
import { sysmsgHandlers } from './smg/sysmsgHandlers'
import { userHandlers } from './user/userHandlers'
import { orgHandlers } from './org/orgHandlers'
import { prodHandlers } from './prod/prodHandlers'
import { screenHandlers } from './screen/screenHandlers'
import { componentHandlers } from '@/mocks/prod/catalog/componentHandlers.ts'
import { resourceHandlers } from '@/mocks/prod/catalog/resourceHandlers.ts'
import { offerHandlers } from '@/mocks/prod/catalog/offerHandlers.ts'
import { treeViewHandlers } from '@/mocks/prod/category/treeViewHandlers.ts'


export const handlers = [
  ...vocaHandlers,
  ...mockDomainHandlers,
  ...cmcdHandlers,
  ...sysmsgHandlers,
  ...userHandlers,
  ...orgHandlers,
  ...prodHandlers,
  ...screenHandlers,
  ...offerHandlers,
  ...componentHandlers,
  ...resourceHandlers,
  ...treeViewHandlers
];
